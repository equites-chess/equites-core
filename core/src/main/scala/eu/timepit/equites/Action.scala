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

sealed trait Action {
  def piece: AnyPiece
  def draw: Draw

  def placedPiece: Placed[AnyPiece] = Placed(piece, draw.src)
}

sealed trait MoveLike extends Action

sealed trait CaptureLike extends MoveLike {
  def captured: AnyPiece

  def capturedOn: Square = draw.dest
  def placedCaptured: Placed[AnyPiece] = Placed(captured, capturedOn)
}

sealed trait PromotionLike extends MoveLike {
  def piece: AnyPawn
  def promotedTo: PromotedPiece

  def placedPromoted: Placed[PromotedPiece] = Placed(promotedTo, draw.dest)
}

case class Move(
  piece: AnyPiece,
  draw: Draw)
    extends MoveLike

case class Capture(
  piece: AnyPiece,
  draw: Draw,
  captured: AnyPiece)
    extends CaptureLike

case class Promotion(
  piece: AnyPawn,
  draw: Draw,
  promotedTo: PromotedPiece)
    extends PromotionLike

case class CaptureAndPromotion(
  piece: AnyPawn,
  draw: Draw,
  captured: AnyPiece,
  promotedTo: PromotedPiece)
    extends CaptureLike with PromotionLike

case class EnPassant(
  piece: AnyPawn,
  draw: Draw,
  captured: AnyPawn,
  override val capturedOn: Square)
    extends CaptureLike

sealed trait Castling extends Action {
  def piece: AnyKing = king
  def draw: Draw = kingMove.draw

  def color: Color
  def side: Side

  def king: AnyKing = Piece(color, King)
  def rook: AnyRook = Piece(color, Rook)

  def kingMove: Action = moveOf(king)
  def rookMove: Action = moveOf(rook)

  def moveOf(piece: CastlingPiece): Action =
    Move(piece, Rules.castlingDraws(side -> piece))
}

case class CastlingShort(color: Color) extends Castling {
  def side: Side = Kingside
}

case class CastlingLong(color: Color) extends Castling {
  def side: Side = Queenside
}
