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

sealed trait Action

sealed trait MoveLike extends Action {
  require(from != to)
  def draw: Draw
  def from = draw.from
  def to = draw.to
  def direction = draw.direction
  def l1Length = draw.l1Length
  def squares = draw.squares
  def piece: AnyPiece
  def placedPiece: Placed[AnyPiece] = Placed(piece, draw.from)
}

sealed trait PromotionLike extends MoveLike {
  require(piece isFriendOf promotedTo)
  def piece: AnyPawn
  def promotedTo: PromotedPiece
  def placedPromoted: Placed[PromotedPiece] = Placed(promotedTo, to)
}

sealed trait CaptureLike extends MoveLike {
  require(piece isOpponentOf captured)
  def captured: AnyPiece
  def capturedOn: Square = to
  def placedCaptured: Placed[AnyPiece] = Placed(captured, capturedOn)
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

object Capture {
  def apply(move: MoveLike, captured: AnyPiece): Capture =
    Capture(move.piece, move.draw, captured)
}

object CaptureAndPromotion {
  def apply(promo: PromotionLike, captured: AnyPiece): CaptureAndPromotion =
    CaptureAndPromotion(promo.piece, promo.draw, captured, promo.promotedTo)
}

sealed trait Castling extends Action {
  def color: Color
  def side: Side

  def king: AnyKing = Piece(color, King)
  def rook: AnyRook = Piece(color, Rook)

  def kingMove: Move = moveOf(king)
  def rookMove: Move = moveOf(rook)

  private[this] def moveOf(piece: CastlingPiece): Move =
    Rules.castlingSquares(side -> piece) match {
      case (from, to) => Move(piece, Draw(from, to))
    }
}

case class CastlingShort(color: Color) extends Castling {
  def side: Side = Kingside
}

case class CastlingLong(color: Color) extends Castling {
  def side: Side = Queenside
}
