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

case class Capture(piece: Piece, from: Field, to: Field, captured: Piece)
  extends Action with MoveLike

case class Promotion(pawn: Pawn, from: Field, to: Field, newPiece: Piece)
  extends Action with MoveLike

case class EnPassant(pawn: Pawn, from: Field, to: Field,
  captured: Pawn, capturedOn: Field)
  extends Action with MoveLike

// sealed abstract class Castling

case class CastlingShort(king: King, rook: Rook) extends Action {
  // Implement backRankBy(color) / pawnRank in Rules object
  private val backRank = if (king.color == White) 0 else 7

  val kingFrom = Field(4, backRank)
  val kingTo   = Field(6, backRank)

  val rookFrom = Field(7, backRank)
  val rookTo   = Field(5, backRank)
}

case class CastlingLong(king: King, rook: Rook) extends Action {
  private val backRank = if (king.color == White) 0 else 7

  val kingFrom = Field(4, backRank)
  val kingTo   = Field(2, backRank)

  val rookFrom = Field(0, backRank)
  val rookTo   = Field(3, backRank)
}
