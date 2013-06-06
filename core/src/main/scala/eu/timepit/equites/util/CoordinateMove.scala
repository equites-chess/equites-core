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
package util

import implicits.PieceImplicits._
import implicits.SquareImplicits._

object CoordinateMove {
  def apply(action: Action): CoordinateMove = action match {
    case p: PromotionLike
      => CoordinateMove(p.from, p.to, Some(p.promotedTo))
    case m: MoveLike
      => CoordinateMove(m.from, m.to, None)
    case c: Castling
      => CoordinateMove(c.kingMove)
  }
}

/**
 * Represents a move in coordinate notation.
 */
case class CoordinateMove(from: Square, to: Square,
  promotedTo: Option[PromotedPiece] = None) extends DrawLike {

  override def toString: String = from.toAlgebraic + to.toAlgebraic +
    promotedTo.map(_.toLowerCaseLetter).getOrElse("")
}
