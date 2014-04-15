// Equites, a Scala chess playground
// Copyright © 2011, 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import scalaz._

trait PieceTypeInstances {
  implicit val pieceTypeShow = Show.showFromToString[PieceType]
}

object PieceType extends PieceTypeInstances {
  def all: List[PieceType] = List(King, Queen, Rook, Bishop, Knight, Pawn)
  def allCastling: List[CastlingPieceType] = List(King, Rook)
  def allPromoted: List[PromotedPieceType] = List(Queen, Rook, Bishop, Knight)
}

sealed trait PieceType
sealed trait CastlingPieceType extends PieceType
sealed trait PromotedPieceType extends PieceType

// format: OFF
case object King   extends CastlingPieceType
case object Queen  extends PromotedPieceType
case object Rook   extends CastlingPieceType with PromotedPieceType
case object Bishop extends PromotedPieceType
case object Knight extends PromotedPieceType
case object Pawn   extends PieceType
// format: ON
