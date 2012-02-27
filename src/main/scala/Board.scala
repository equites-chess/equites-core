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

import scala.collection._

class Board extends ActionListener with Iterable[(Square, Piece)] {
  def contains(piece: Piece): Boolean =
    squaresMap.contains(piece) || takenSet.contains(piece)

  def occupied(square: Square): Boolean = piecesMap.contains(square)

  def occupiedBy(square: Square, piece: Piece): Boolean =
    occupied(square) && piecesMap(square) == piece

  def opponentAt(square: Square, color: Color): Boolean =
    occupied(square) && piecesMap(square).color != color

  def getPiece(square: Square): Option[Piece] = piecesMap.get(square)
  def getSquare(piece: Piece): Option[Square] = squaresMap.get(piece)

  def putPiece(square: Square, piece: Piece) {
    require(!occupied(square) && !contains(piece))
    update(square, piece)
  }

  def putPieces(pieces: Traversable[(Square, Piece)]) {
    pieces.foreach { case (square, piece) => putPiece(square, piece) }
  }

  def removePiece(square: Square): Option[Piece] =
    removeFromBoth(square, piecesMap, squaresMap)

  def removePiece(piece: Piece): Option[Square] = {
    takenSet.remove(piece)
    removeFromBoth(piece, squaresMap, piecesMap)
  }

  def removePieces(pieces: Traversable[Piece]) {
    pieces.foreach(removePiece(_))
  }

  def clear() {
    piecesMap.clear()
    squaresMap.clear()
    takenSet.clear()
  }

  override def isEmpty: Boolean = piecesMap.isEmpty && takenSet.isEmpty

  def iterator: Iterator[(Square, Piece)] = piecesMap.iterator

  def processAction(move: MoveLike) {
    doMove(move.piece, move.from, move.to)
  }

  def reverseAction(move: MoveLike) {
    undoMove(move.piece, move.from, move.to)
  }

  def processAction(promo: PromotionLike) {
    doMove(promo.piece, promo.from, promo.to)
    doPromotion(promo.to, promo.piece, promo.newPiece)
  }

  def reverseAction(promo: PromotionLike) {
    undoPromotion(promo.to, promo.piece, promo.newPiece)
    undoMove(promo.piece, promo.from, promo.to)
  }

  def processAction(capt: CaptureLike) {
    doCapture(capt.captured, capt.capturedOn)
    doMove(capt.piece, capt.from, capt.to)
  }

  def reverseAction(capt: CaptureLike) {
    undoMove(capt.piece, capt.from, capt.to)
    undoCapture(capt.captured, capt.capturedOn)
  }

  def processAction(capt: CaptureAndPromotion) {
    doCapture(capt.captured, capt.capturedOn)
    doMove(capt.piece, capt.from, capt.to)
    doPromotion(capt.to, capt.piece, capt.newPiece)
  }

  def reverseAction(capt: CaptureAndPromotion) {
    undoPromotion(capt.to, capt.piece, capt.newPiece)
    undoMove(capt.piece, capt.from, capt.to)
    undoCapture(capt.captured, capt.capturedOn)
  }

  def processAction(castling: Castling) {
    processAction(castling.kingMove)
    processAction(castling.rookMove)
  }

  def reverseAction(castling: Castling) {
    reverseAction(castling.rookMove)
    reverseAction(castling.kingMove)
  }

  private def doMove(piece: Piece, from: Square, to: Square) {
    require(occupiedBy(from, piece) && !occupied(to))

    piecesMap.remove(from)
    update(to, piece)
  }

  private def undoMove(piece: Piece, from: Square, to: Square) {
    doMove(piece, to, from)
  }

  private def doPromotion(on: Square, oldPiece: Piece, newPiece: Piece) {
    require(occupiedBy(on, oldPiece) && !contains(newPiece))

    squaresMap.remove(oldPiece)
    update(on, newPiece)
  }

  private def undoPromotion(on: Square, oldPiece: Piece, newPiece: Piece) {
    doPromotion(on, newPiece, oldPiece)
  }

  private def doCapture(captured: Piece, capturedOn: Square) {
    require(occupiedBy(capturedOn, captured) && !takenSet.contains(captured))

    piecesMap.remove(capturedOn)
    squaresMap.remove(captured)
    takenSet.add(captured)
  }

  private def undoCapture(captured: Piece, capturedOn: Square) {
    require(takenSet.contains(captured) && !occupied(capturedOn))

    takenSet.remove(captured)
    update(capturedOn, captured)
  }

  private def update(square: Square, piece: Piece) {
    piecesMap(square) = piece
    squaresMap(piece) = square
  }

  private def removeFromBoth[A, B](key1: A,
    map1: mutable.Map[A, B], map2: mutable.Map[B, A]): Option[B] = {

    val result = map1.remove(key1)
    if (result != None) map2.remove(result.get)
    result
  }

  private val piecesMap: mutable.Map[Square, Piece] = mutable.Map()
  private val squaresMap: mutable.Map[Piece, Square] = mutable.Map()
  private val takenSet: mutable.Set[Piece] = mutable.Set()
}
