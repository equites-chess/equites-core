// Equites, a simple chess interface
// Copyright © 2011, 2013 Frank S. Thomas <f.thomas@gmx.de>
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

import implicits.GenericImplicits._

object Notation {
  val algebraicFileRange: NumericRange[Char] =
    (Rules.fileRange.start + 'a').toChar to
    (Rules.fileRange.end   + 'a').toChar

  val algebraicRankRange: Range =
    (Rules.rankRange.start + 1) to
    (Rules.rankRange.end   + 1)

  val numericFileRange: Range =
    (Rules.fileRange.start + 1) to
    (Rules.fileRange.end   + 1)

  def numericRankRange: Range = algebraicRankRange

  def pieceFromFigurine(c: Char): Option[Piece] = c match {
    case '\u2654' => Some(King(White))
    case '\u2655' => Some(Queen(White))
    case '\u2656' => Some(Rook(White))
    case '\u2657' => Some(Bishop(White))
    case '\u2658' => Some(Knight(White))
    case '\u2659' => Some(Pawn(White))
    case '\u265A' => Some(King(Black))
    case '\u265B' => Some(Queen(Black))
    case '\u265C' => Some(Rook(Black))
    case '\u265D' => Some(Bishop(Black))
    case '\u265E' => Some(Knight(Black))
    case '\u265F' => Some(Pawn(Black))
    case _ => None
  }

  def squareFromAlgebraic(s: String): Option[Square] = {
    val valid = s.length == 2 &&
      algebraicFileRange.contains(s(0)) &&
      algebraicRankRange.contains(s(1).asDigit)

    if (valid) Some(Square(s(0), s(1).asDigit)) else None
  }

  def moveFromLongFigurine(s: String): Option[Move] = {
    for {
      fstChar <- s.headOption
      piece   <- pieceFromFigurine(fstChar)
      square1 <- s.slice(1, 2).asOption
      square2 <- s.slice(4, 5).asOption
      from    <- squareFromAlgebraic(square1)
      to      <- squareFromAlgebraic(square2)
    } yield Move(piece, from, to)
  }
}
