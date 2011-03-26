// Equites, a simple chess interface
// Copyright © 2011 Frank S. Thomas <f.thomas@gmx.de>
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

package equites

sealed abstract class Action

trait MoveLike {
  def from: Field
  def to: Field

  def diff: Vector = to - from
  def l1Dist: Int = Field.l1Dist(from, to)
  def lInfDist: Int = Field.lInfDist(from, to)
}

case class Move(piece: Piece, from: Field, to: Field)
  extends Action with MoveLike

case class Promotion(pawn: Pawn, from: Field, to: Field)
  extends Action with MoveLike {

  var newPiece: Piece = new Queen(pawn.color)
}

case class Capture(piece: Piece, from: Field, to: Field, captured: Piece)
  extends Action with MoveLike

case class EnPassant(pawn: Pawn, from: Field, to: Field,
  captured: Pawn, capturedOn: Field)
  extends Action with MoveLike


sealed abstract class Side
case object Kingside  extends Side
case object Queenside extends Side

sealed abstract class Castling(side: Side) extends Action {
  def king: King
  def rook: Rook

  val kingMove: Move = constructMove(king)
  val rookMove: Move = constructMove(rook)

  private def constructMove(piece: Piece): Move = {
    val (from, to) =
      Rules.castlingFields((side, piece.color, piece.pieceType))
    Move(piece, from, to)
  }
}

case class CastlingShort(king: King, rook: Rook) extends Castling(Kingside)
case class CastlingLong (king: King, rook: Rook) extends Castling(Queenside)
