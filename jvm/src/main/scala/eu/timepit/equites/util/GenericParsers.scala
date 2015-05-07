// Equites, a Scala chess playground
// Copyright © 2013-2015 Frank S. Thomas <frank@timepit.eu>
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

import eu.timepit.equites.util.PieceUtil._
import eu.timepit.equites.util.SquareUtil._

import scala.util.parsing.combinator.RegexParsers

trait GenericParsers extends RegexParsers {
  def choice[A](ps: Seq[Parser[A]]): Parser[A] =
    ps.fold(failure("choice: empty sequence"))(_ | _)

  def oneOf[A](as: Seq[A])(toLiteral: A => String): Parser[A] =
    choice(as.map(a => toLiteral(a) ^^^ a))

  def integer: Parser[Int] =
    """-?\d+""".r ^^ (_.toInt)

  def nonNegativeInteger: Parser[Int] =
    """\d+""".r ^^ (_.toInt)

  def algebraicFile: Parser[File] =
    oneOf(algebraicFileSeq)(_.value.toString).map(fileFromAlgebraic)

  def algebraicRank: Parser[Rank] =
    oneOf(algebraicRankSeq)(_.value.toString).map(rankFromAlgebraic)

  def algebraicSquare: Parser[Square] =
    algebraicFile ~ algebraicRank ^^ {
      case file ~ rank => Square.unsafeFrom(file, rank)
    }

  def lowerCasePromotedPieceType: Parser[PromotedPieceType] =
    oneOf(PieceType.allPromoted)(showLowerCaseLetter)

  def upperCasePieceType: Parser[PieceType] =
    oneOf(PieceType.all)(showUpperCaseLetter)

  def upperCasePromotedPieceType: Parser[PromotedPieceType] =
    oneOf(PieceType.allPromoted)(showUpperCaseLetter)
}
