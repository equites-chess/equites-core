// Equites, a Scala chess playground
// Copyright © 2013 Frank S. Thomas <frank@timepit.eu>
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
package proto

import scala.util.parsing.combinator._

import Uci._
import util.Notation._

/**
 * Parsers for the Universal Chess Interface (UCI).
 */
object UciParsers extends RegexParsers {
  def symbol: Parser[String] = """\p{Alnum}+""".r

  def string: Parser[String] = """.*""".r

  val algebraicFile: Parser[Char] = oneOf(algebraicFileRange) ^^ (_.charAt(0))

  val algebraicRank: Parser[Int] = oneOf(algebraicRankRange) ^^ (_.toInt)

  def square: Parser[Square] = algebraicFile ~ algebraicRank  ^^ {
    case file ~ rank => Square(file, rank)
  }

  def promotedPieceFn: Parser[Color => PromotedPiece] =
    "q" ^^^ (Queen(_))  |
    "r" ^^^ (Rook(_))   |
    "b" ^^^ (Bishop(_)) |
    "n" ^^^ (Knight(_))

  def coordinateMove: Parser[util.CoordinateMove] =
    square ~ square ~ promotedPieceFn.? ^^ {
      case from ~ to ~ pieceFn => {
        val piece = pieceFn.map { piece =>
          val color = Color.guessFrom(to - from)
          piece(color)
        }
        util.CoordinateMove(from, to, piece)
      }
    }

  def id: Parser[Id] = "id" ~> symbol ~ string ^^ {
    case key ~ value => Id(key, value)
  }

  def uciok: Parser[UciOk.type] = "uciok" ^^^ UciOk

  def readyok: Parser[ReadyOk.type] = "readyok" ^^^ ReadyOk

  def bestmove: Parser[Bestmove] =
    "bestmove" ~> coordinateMove ~ ("ponder" ~> coordinateMove).? ^^ {
      case move ~ ponder => Bestmove(move, ponder)
    }

  private def oneOf[A](as: Seq[A]): Parser[String] = as.mkString("|").r
}
