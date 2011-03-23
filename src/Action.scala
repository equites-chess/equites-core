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

sealed abstract class Castling extends Action {
  val king: King
  val rook: Rook

  val kingMove = Move(king, kingFrom, kingTo)
  val rookMove = Move(rook, rookFrom, rookTo)

  protected val kingFrom: Field
  protected val kingTo: Field

  protected val rookFrom: Field
  protected val rookTo: Field
}

case class CastlingShort(king: King, rook: Rook) extends Castling {
  import Rules._
  private val backRank = backRankBy(king.color)

  protected val kingFrom = Field(kingFile, backRank)
  protected val kingTo   = Field(castlingShortFile, backRank)

  protected val rookFrom = Field(rookFiles(1), backRank)
  protected val rookTo   = Field(castlingShortFile - 1, backRank)
}

case class CastlingLong(king: King, rook: Rook) extends Castling {
  import Rules._
  private val backRank = backRankBy(king.color)

  protected val kingFrom = Field(kingFile, backRank)
  protected val kingTo   = Field(castlingLongFile, backRank)

  protected val rookFrom = Field(rookFiles(0), backRank)
  protected val rookTo   = Field(castlingLongFile + 1, backRank)
}
