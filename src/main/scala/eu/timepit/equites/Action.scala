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

sealed trait Action

sealed trait MoveLike extends Action {
  def piece: Piece
  def from: Square
  def to: Square

  def diff: Vec = to - from
  def l1Length: Int = diff.l1Length
  def lInfLength: Int = diff.lInfLength
}

sealed trait CaptureLike extends MoveLike {
  require(piece isOpponentOf captured)

  def captured: Piece
  def capturedOn: Square = to
}

sealed trait PromotionLike extends MoveLike {
  require(piece isFriendOf promotedTo)

  def piece: Pawn
  def promotedTo: Piece
}

case class Move(piece: Piece, from: Square, to: Square)
  extends MoveLike

case class Promotion(piece: Pawn, from: Square, to: Square, promotedTo: Piece)
  extends PromotionLike

case class Capture(piece: Piece, from: Square, to: Square, captured: Piece)
  extends CaptureLike

case class CaptureAndPromotion(piece: Pawn, from: Square, to: Square,
  captured: Piece, promotedTo: Piece)
  extends CaptureLike with PromotionLike

case class EnPassant(piece: Pawn, from: Square, to: Square, captured: Pawn,
  override val capturedOn: Square)
  extends CaptureLike

sealed trait Side
case object Kingside  extends Side
case object Queenside extends Side

sealed trait Castling extends Action {
  def color: Color
  def side: Side

  def king = King(color)
  def rook = Rook(color)

  def kingMove: Move = moveOf(king)
  def rookMove: Move = moveOf(rook)

  private def moveOf(piece: Piece): Move = {
    val (from, to) = Rules.castlingSquares((side, piece))
    Move(piece, from, to)
  }
}

case class CastlingShort(color: Color) extends Castling {
  def side = Kingside
}

case class CastlingLong(color: Color) extends Castling {
  def side = Queenside
}
