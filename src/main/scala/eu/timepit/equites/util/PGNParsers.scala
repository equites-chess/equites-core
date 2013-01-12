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

object PGNParsers extends RegexParsers {
  def integer: Parser[Int] =
    """\d+""".r ^^ (_.toInt)

  def string: Parser[String] =
    """"\w*"""".r

  def symbol: Parser[String] =
    """[\d\p{Alpha}][\w+#=:-]{0,254}""".r

  def tagPair: Parser[(String, String)] = {
    val fst = "[" ~> whiteSpace.? ~> symbol <~ whiteSpace.?
    val snd = string <~ whiteSpace.? <~ "]"

    fst ~ snd ^^ (x => (x._1, x._2))
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
    """(1-0|0-1|1/2-1/2|\*)""".r
}
