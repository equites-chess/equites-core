// Equites, a Scala chess playground
// Copyright © 2011, 2013-2015 Frank S. Thomas <frank@timepit.eu>
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

case class Board(self: Map[Square, AnyPiece]) extends AnyVal {
  def +(kv: (Square, AnyPiece)): Board =
    Board(self + kv)

  def +(placed: PlacedPiece): Board =
    this + placed.toTuple

  def -(square: Square): Board =
    Board(self - square)

  def get(key: Square): Option[AnyPiece] =
    self.get(key)

  def getPlaced(square: Square): Option[PlacedPiece] =
    get(square).map(Placed(_, square))

  def getPlaced(squares: Seq[Square]): Seq[PlacedPiece] =
    squares.map(getPlaced).flatten

  def isVacant(square: Square): Boolean =
    !isOccupied(square)

  def isOccupied(square: Square): Boolean =
    self.contains(square)

  def isOccupiedBy(square: Square, piece: AnyPiece): Boolean =
    get(square).contains(piece)

  def placedPieces: Stream[PlacedPiece] =
    self.toStream.map { case (square, piece) => Placed(piece, square) }

  def pieceCount: Int =
    self.size

  def processAction(action: Action): Board =
    action match {
      case a: CaptureAndPromotion => processCaptureAndPromotion(a)
      case a: CaptureLike         => processCapture(a)
      case a: PromotionLike       => processPromotion(a)
      case a: MoveLike            => processMove(a)
      case a: Castling            => processCastling(a)
    }

  def reverseAction(action: Action): Board =
    action match {
      case a: CaptureAndPromotion => reverseCaptureAndPromotion(a)
      case a: CaptureLike         => reverseCapture(a)
      case a: PromotionLike       => reversePromotion(a)
      case a: MoveLike            => reverseMove(a)
      case a: Castling            => reverseCastling(a)
    }

  def processMove(move: MoveLike): Board =
    this - move.draw.src + (move.draw.dest -> move.piece)

  def reverseMove(move: MoveLike): Board =
    this - move.draw.dest + (move.draw.src -> move.piece)

  def processPromotion(promotion: PromotionLike): Board =
    this - promotion.draw.src + (promotion.draw.dest -> promotion.promotedTo)

  def reversePromotion(promotion: PromotionLike): Board =
    this - promotion.draw.dest + (promotion.draw.src -> promotion.piece)

  def processCapture(capture: CaptureLike): Board =
    this - capture.draw.src - capture.capturedOn + (capture.draw.dest -> capture.piece)

  def reverseCapture(capture: CaptureLike): Board =
    this - capture.draw.dest + (capture.capturedOn -> capture.captured) +
      (capture.draw.src -> capture.piece)

  def processCaptureAndPromotion(capture: CaptureAndPromotion): Board =
    this - capture.draw.src - capture.capturedOn +
      (capture.draw.dest -> capture.promotedTo)

  def reverseCaptureAndPromotion(capture: CaptureAndPromotion): Board =
    reverseCapture(capture: CaptureLike)

  def processCastling(castling: Castling): Board =
    processAction(castling.kingMove).processAction(castling.rookMove)

  def reverseCastling(castling: Castling): Board =
    reverseAction(castling.kingMove).reverseAction(castling.rookMove)
}

object Board {
  def apply(kvs: (Square, AnyPiece)*): Board =
    Board(kvs.toMap)

  val empty: Board =
    Board(Map.empty[Square, AnyPiece])
}
