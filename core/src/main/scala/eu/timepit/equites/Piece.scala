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

import scalaz._
import Scalaz._

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

object Piece {
  def allTypes: List[PieceType] =
    List(King, Queen, Rook, Bishop, Knight, Pawn)

  def allCastlingTypes: List[CastlingPieceType] =
    List(King, Rook)

  def allPromotedTypes: List[PromotedPieceType] =
    List(Queen, Rook, Bishop, Knight)

  def all: List[AnyPiece] = genAllPieces(allTypes)
  def allCastling: List[CastlingPiece] = genAllPieces(allCastlingTypes)
  def allPromoted: List[PromotedPiece] = genAllPieces(allPromotedTypes)

  private def genAllPieces[T <: PieceType](pieceTypes: List[T]): List[Piece[Color, T]] =
    (Color.all |@| pieceTypes) { Piece.apply }
}

case class Piece[+C <: Color, +T <: PieceType](color: C, pieceType: T) {
  def isFriendOf(other: AnyPiece): Boolean = color == other.color
  def isOpponentOf(other: AnyPiece): Boolean = color != other.color

  // format: OFF
  def isKing   = is(King)
  def isQueen  = is(Queen)
  def isRook   = is(Rook)
  def isBishop = is(Bishop)
  def isKnight = is(Knight)
  def isPawn   = is(Pawn)

  def maybeKing   = maybe(King)
  def maybeQueen  = maybe(Queen)
  def maybeRook   = maybe(Rook)
  def maybeBishop = maybe(Bishop)
  def maybeKnight = maybe(Knight)
  def maybePawn   = maybe(Pawn)
  // format: ON

  private[this] def is(pType: PieceType): Boolean = pieceType == pType

  private[this] def maybe[T1 <: PieceType](pType: T1): Option[Piece[C, T1]] =
    (pieceType == pType).option(Piece(color, pType))
}
