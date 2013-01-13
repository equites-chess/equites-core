// Equites, a simple chess interface
// Copyright © 2013 Frank S. Thomas <f.thomas@gmx.de>
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

/**
 * Parsers for the Portable Game Notation (PGN).
 *
 * @see [[http://www.chessclub.com/help/PGN-spec Portable Game Notation Specification and Implementation Guide]]
 */
object PGNParsers extends RegexParsers {
  def integer: Parser[Int] =
    """\d+""".r ^^ (_.toInt)

  def string: Parser[String] =
    """"([^"\\]|\\.)*"""".r ^^ { _.drop(1).dropRight(1)
      .replaceAllLiterally("\\\\", "\\")
      .replaceAllLiterally("\\\"", "\"")
    }

  def symbol: Parser[String] =
    """[\d\p{Alpha}][\w+#=:-]*""".r

  def tagPair: Parser[(String, String)] = {
    val name  = withMaybeWS(symbol)
    val value = withMaybeWS(string)
    "[" ~> name ~ value <~ "]" ^^ toTuple
  }

  def blockComment: Parser[String] =
    """\{[^}]*\}""".r

  def lineComment: Parser[String] =
    """;.*$""".r

  def moveText = ???

  def moveNumberIndicator: Parser[Any] =
    integer ~ """(\.{3}|\.)""".r

  def moveSymbol =
    """(\p{Print}?x?[a-z]\d(=\p{Print})?|O(-O){1,2})""".r

  def moveAnnotation: Parser[String] =
    """[!?]{1,2}""".r

  def numericAnnotationGlyph: Parser[Any] =
    "$" ~ integer

  def terminationMarker: Parser[String] =
    "1-0" | "0-1" | "1/2-1/2" | "*"


  def withMaybeWS[T](p: Parser[T]): Parser[T] =
    whiteSpace.? ~> p <~ whiteSpace.?

  def toTuple[T, U](seq: T ~ U): (T, U) = (seq._1, seq._2)
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
