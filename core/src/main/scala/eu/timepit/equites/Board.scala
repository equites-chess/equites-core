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

import scala.collection.immutable.MapProxy

object Board {
  def apply(mapping: Map[Square, Piece]): Board = new Board(mapping)
  def apply(kvs: (Square, Piece)*): Board = new Board(kvs.toMap)
  val empty: Board = Board()
}

class Board(val self: Map[Square, Piece]) extends MapProxy[Square, Piece] {
  def getPlaced(square: Square): Option[Placed[Piece]] =
    get(square).map(Placed(_, square))

  def getPlaced(squares: Seq[Square]): Seq[Placed[Piece]] =
    squares.map(getPlaced).flatten

  def isVacant(square: Square): Boolean = !isOccupied(square)
  def isOccupied(square: Square): Boolean = contains(square)

  def isOccupiedBy(square: Square, piece: Piece): Boolean =
    get(square).exists(_ == piece)

  def placedPieces: Stream[Placed[Piece]] =
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
    this - move.from + (move.to -> move.piece)

  def reverseMove(move: MoveLike): Board =
    this - move.to + (move.from -> move.piece)

  def processPromotion(promotion: PromotionLike): Board =
    this - promotion.from + (promotion.to -> promotion.promotedTo)

  def reversePromotion(promotion: PromotionLike): Board =
    this - promotion.to + (promotion.from -> promotion.piece)

  def processCapture(capture: CaptureLike): Board =
    this - capture.from - capture.capturedOn + (capture.to -> capture.piece)

  def reverseCapture(capture: CaptureLike): Board =
    this - capture.to + (capture.capturedOn -> capture.captured) +
      (capture.from -> capture.piece)

  def processCaptureAndPromotion(capture: CaptureAndPromotion): Board =
    this - capture.from - capture.capturedOn +
      (capture.to -> capture.promotedTo)

  def reverseCaptureAndPromotion(capture: CaptureAndPromotion): Board =
    reverseCapture(capture: CaptureLike)

  def processCastling(castling: Castling): Board =
    processMove(castling.kingMove).processMove(castling.rookMove)

  def reverseCastling(castling: Castling): Board =
    reverseMove(castling.kingMove).reverseMove(castling.rookMove)

  def +(kv: (Square, Piece)): Board =
    Board(self + kv)

  def +(placed: Placed[Piece]): Board =
    Board(self + (placed.square -> placed.elem))

  override def -(square: Square): Board =
    Board(self - square)
}
