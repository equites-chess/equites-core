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

import scala.util.parsing.combinator._

import implicits.GenericImplicits._
import util.Pgn._

/**
 * Parsers for the Portable Game Notation (PGN).
 *
 * @see [[http://www.chessclub.com/help/PGN-spec Portable Game Notation Specification and Implementation Guide]]
 */
object PgnParsers extends RegexParsers {
  def integer: Parser[Int] =
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
    "[" ~> tagName ~ tagValue <~ "]" ^^ {
      case name ~ value => Tag(name, value)
    }

  def tagSection: Parser[List[Tag]] =
    (tagPair <~ comment.*).*

  def moveNumberIndicator: Parser[MoveNumberIndicator] = {
    def white = "." ^^^ White
    def black = "..." ^^^ Black
    integer ~ (black | white) ^^ {
      case number ~ color => MoveNumberIndicator(number, color)
    }
  }

  def sanMove: Parser[SanMove] =
    """(\p{Print}?([a-z]?\d?|)x?[a-z]\d(=\p{Print})?|O(-O){1,2})[+#]?""".r ^^ SanMove

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
    "$" ~> integer ^^ AnnotationGlyph

  def terminationMarker: Parser[GameResult] =
    GameResult.all.map(r => GameResultUtil.showPgnMarker(r) ^^^ r).reduce(_ | _)

  def moveTextElem: Parser[MoveElement] =
    moveNumberIndicator | sanMove | moveAnnotation | numericAnnotationGlyph

  def moveTextSeq: Parser[List[SeqElem]] =
    (moveTextElem | blockComment | recursiveVariation).*

  def recursiveVariation: Parser[RecursiveVariation] =
    "(" ~> moveTextSeq <~ ")" ^^ RecursiveVariation

  def moveTextSection: Parser[MoveTextSection] =
    moveTextSeq ~ terminationMarker ^^ {
      case moveText ~ result => MoveTextSection(moveText, result)
    }

  def gameRecord: Parser[GameRecord] =
    tagSection ~ moveTextSection ^^ {
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
