// Equites, a Scala chess playground
// Copyright © 2011, 2013 Frank S. Thomas <frank@timepit.eu>
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

import scala.collection.immutable.NumericRange

import PieceAbbr.Algebraic._
import PieceAbbr.Figurine._

object Notation {
  val algebraicFileRange: NumericRange[Char] =
    (Rules.fileRange.start + 'a').toChar to
      (Rules.fileRange.end + 'a').toChar

  val algebraicRankRange: Range =
    (Rules.rankRange.start + 1) to
      (Rules.rankRange.end + 1)

  val numericFileRange: Range =
    (Rules.fileRange.start + 1) to
      (Rules.fileRange.end + 1)

  def numericRankRange: Range = algebraicRankRange

  def pieceFromLetter(c: Char): Option[Piece] = c match {
    case 'K' => Some(K)
    case 'Q' => Some(Q)
    case 'R' => Some(R)
    case 'B' => Some(B)
    case 'N' => Some(N)
    case 'P' => Some(P)
    case 'k' => Some(k)
    case 'q' => Some(q)
    case 'r' => Some(r)
    case 'b' => Some(b)
    case 'n' => Some(n)
    case 'p' => Some(p)
    case _   => None
  }

  def pieceFromFigurine(c: Char): Option[Piece] = c match {
    case '♔' => Some(♔)
    case '♕' => Some(♕)
    case '♖' => Some(♖)
    case '♗' => Some(♗)
    case '♘' => Some(♘)
    case '♙' => Some(♙)
    case '♚' => Some(♚)
    case '♛' => Some(♛)
    case '♜' => Some(♜)
    case '♝' => Some(♝)
    case '♞' => Some(♞)
    case '♟' => Some(♟)
    case _   => None
  }

  def boardFromFen(placement: String): Board = {
    def expandDigits(target: String): String =
      """\d""".r.replaceAllIn(target, "1" * _.toString.toInt)

    val mapping = for {
      (rankStr, rank) <- placement.split("/").reverse.zipWithIndex
      (pieceChar, file) <- expandDigits(rankStr).zipWithIndex
      piece <- pieceFromLetter(pieceChar)
    } yield Square(file, rank) -> piece

    Board(mapping: _*)
  }
}
