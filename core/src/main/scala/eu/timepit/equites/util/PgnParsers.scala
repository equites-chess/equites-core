// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package eu.timepit.equites
package util

import implicits.GenericImplicits._
import util.Pgn._

/**
 * Parsers for the Portable Game Notation (PGN).
 *
 * @see [[http://www.chessclub.com/help/PGN-spec Portable Game Notation Specification and Implementation Guide]]
 */
object PgnParsers extends GenericParsers {
  override val skipWhitespace = false

  def ws = """\s*""".r

  def nonNegativeInteger: Parser[Int] =
    """\d+""".r ^^ (_.toInt)

  def string: Parser[String] =
    """"([^"\\]|\\.)*"""".r ^^ {
      _.dropLeftRight(1)
        .replaceAllLiterally("\\\\", "\\")
        .replaceAllLiterally("\\\"", "\"")
    }

  def symbol: Parser[String] =
    """[\d\p{Alpha}][\w+#=:-]*""".r

  def blockComment: Parser[Comment] =
    """\{[^}]*\}""".r ^^ (text => Comment(text.dropLeftRight(1)))

  def lineComment: Parser[Comment] =
    """;.*""".r ^^ (text => Comment(text.drop(1)))

  def comment: Parser[Comment] =
    blockComment | lineComment

  def tagName: Parser[String] =
    symbol

  def tagValue: Parser[String] =
    string

  def tagPair: Parser[Tag] =
    "[" ~> ws ~> (tagName <~ ws) ~ tagValue <~ ws <~ "]" ^^ {
      case name ~ value => Tag(name, value)
    }

  def tagSection: Parser[List[Tag]] =
    (tagPair <~ ws <~ (comment <~ ws).*).*

  def moveNumber: Parser[MoveNumber] = {
    def white = "." ^^^ White
    def black = "..." ^^^ Black
    nonNegativeInteger ~ (black | white) ^^ {
      case number ~ color => MoveNumber(number, color)
    }
  }

  def sanAction: Parser[SanAction] = {
    def maybeUpperCasePieceType: Parser[PieceType] =
      upperCasePieceType.?.map(_.getOrElse(Pawn))

    def maybeSquare: Parser[MaybeSquare] =
      algebraicFile.? ~ algebraicRank.? ^^ {
        case file ~ rank => MaybeSquare(file, rank)
      }

    def srcSquareParsers: Seq[Parser[MaybeSquare]] =
      Seq(algebraicSquare ^^ MaybeSquare.apply, maybeSquare, success(MaybeSquare()))

    def sanMove: Parser[SanMoveLike] = {
      def capture = "x".? ^^ (_.isDefined)

      def drawAndCapture: Parser[(MaybeDraw, Boolean)] =
        appendSeq(srcSquareParsers.map(_ ~ capture ~ algebraicSquare)) ^^ {
          case src ~ x ~ dest => (MaybeDraw(src, dest), x)
        }

      def promotedTo = ("=" ~> upperCasePromotedPieceType).?

      maybeUpperCasePieceType ~ drawAndCapture ~ promotedTo ^^ {
        case pt ~ ((draw, false)) ~ None           => SanMove(pt, draw)
        case pt ~ ((draw, true)) ~ None            => SanCapture(pt, draw)
        case pt ~ ((draw, false)) ~ Some(promoted) => SanPromotion(pt, draw, promoted)
        case pt ~ ((draw, true)) ~ Some(promoted)  => SanCaptureAndPromotion(pt, draw, promoted)
      }
    }

    def castling: Parser[SanCastling] =
      ("O-O-O" ^^^ Queenside | "O-O" ^^^ Kingside).map(SanCastling)

    def nonCheckingSanAction: Parser[SanAction] =
      sanMove | castling

    def maybeCheckingSanAction: Parser[SanAction] = {
      def checkIndicator = "+" ^^^ Check | "#" ^^^ CheckMate
      nonCheckingSanAction ~ checkIndicator.? ^^ {
        case action ~ indicator =>
          indicator.fold(action)(i => CheckingSanAction(action, i))
      }
    }

    maybeCheckingSanAction
  }

  def moveAnnotation: Parser[AnnotationGlyph] =
    """[!?]{1,2}""".r ^^ {
      case "!"  => 1
      case "?"  => 2
      case "!!" => 3
      case "??" => 4
      case "!?" => 5
      case "?!" => 6
    } map AnnotationGlyph

  def numericAnnotationGlyph: Parser[AnnotationGlyph] =
    "$" ~> nonNegativeInteger ^^ AnnotationGlyph

  def terminationMarker: Parser[GameResult] =
    oneOf(GameResult.all)(GameResultUtil.showPgnMarker)

  def moveTextElem: Parser[MoveElement] =
    moveNumber | sanAction ^^ MoveSymbol | moveAnnotation | numericAnnotationGlyph

  def moveTextSeq: Parser[List[SeqElem]] =
    ((moveTextElem ^^ SeqMoveElement
      | blockComment ^^ SeqComment
      | recursiveVariation) <~ ws).*

  def recursiveVariation: Parser[RecursiveVariation] =
    "(" ~> ws ~> moveTextSeq <~ ws <~ ")" ^^ RecursiveVariation

  def moveTextSection: Parser[MoveTextSection] =
    (ws ~> moveTextSeq <~ ws) ~ terminationMarker ^^ {
      case moveText ~ result => MoveTextSection(moveText, result)
    }

  def gameRecord: Parser[GameRecord] =
    (ws ~> tagSection <~ ws) ~ moveTextSection ^^ {
      case tags ~ moves => GameRecord(tags, moves)
    }
}

/*
from http://www.chessclub.com/help/PGN-spec:

18: Formal syntax

<PGN-database> ::= <PGN-game> <PGN-database>
                   <empty>

<PGN-game> ::= <tag-section> <movetext-section>

<tag-section> ::= <tag-pair> <tag-section>
                  <empty>

<tag-pair> ::= [ <tag-name> <tag-value> ]

<tag-name> ::= <identifier>

<tag-value> ::= <string>

<movetext-section> ::= <element-sequence> <game-termination>

<element-sequence> ::= <element> <element-sequence>
                       <recursive-variation> <element-sequence>
                       <empty>

<element> ::= <move-number-indication>
              <SAN-move>
              <numeric-annotation-glyph>

<recursive-variation> ::= ( <element-sequence> )

<game-termination> ::= 1-0
                       0-1
                       1/2-1/2
                       *
<empty> ::=
*/
