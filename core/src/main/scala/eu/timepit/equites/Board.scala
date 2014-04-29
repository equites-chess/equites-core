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

import scala.collection.immutable.MapProxy

class Board(val self: Map[Square, AnyPiece])
    extends MapProxy[Square, AnyPiece] {

  def getPlaced(square: Square): Option[Placed[AnyPiece]] =
    get(square).map(Placed(_, square))

  def getPlaced(squares: Seq[Square]): Seq[Placed[AnyPiece]] =
    squares.map(getPlaced).flatten

  def isVacant(square: Square): Boolean = !isOccupied(square)
  def isOccupied(square: Square): Boolean = contains(square)

  def isOccupiedBy(square: Square, piece: AnyPiece): Boolean =
    get(square).exists(_ == piece)

  def placedPieces: Stream[Placed[AnyPiece]] =
    toStream.map { case (square, piece) => Placed(piece, square) }

  def processAction(action: Action): Board = action match {
    case a: CaptureAndPromotion => processCaptureAndPromotion(a)
    case a: CaptureLike         => processCapture(a)
    case a: PromotionLike       => processPromotion(a)
    case a: MoveLike            => processMove(a)
    case a: Castling            => processCastling(a)
  }

  def reverseAction(action: Action): Board = action match {
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

  def +(kv: (Square, AnyPiece)): Board =
    Board(self + kv)

  def +(placed: Placed[AnyPiece]): Board =
    Board(self + (placed.square -> placed.elem))

  override def -(square: Square): Board =
    Board(self - square)
}

object Board {
  def apply(mapping: Map[Square, AnyPiece]): Board = new Board(mapping)
  def apply(kvs: (Square, AnyPiece)*): Board = new Board(kvs.toMap)
  val empty: Board = Board()
}
