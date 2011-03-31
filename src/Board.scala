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

class Board extends ActionListener with Iterable[(Field, Piece)] {
  def contains(piece: Piece): Boolean =
    fieldsMap.contains(piece) || taken.contains(piece)

  def occupied(field: Field): Boolean = piecesMap.contains(field)

  def occupiedBy(field: Field, piece: Piece): Boolean =
    occupied(field) && piecesMap(field) == piece

  def opponentAt(field: Field, color: Color): Boolean =
    occupied(field) && piecesMap(field).color != color

  def getPiece(field: Field): Option[Piece] = piecesMap.get(field)
  def getField(piece: Piece): Option[Field] = fieldsMap.get(piece)

  def putPiece(field: Field, piece: Piece) {
    require(!occupied(field) && !contains(piece))

    updateBoth(field, piece)
  }

  def putPieces(pieces: Traversable[(Field, Piece)]) {
    pieces.foreach { case (field, piece) => putPiece(field, piece) }
  }

  def removePiece(field: Field): Option[Piece] =
    removeFromBoth(field, piecesMap, fieldsMap)

  def removePiece(piece: Piece): Option[Field] = {
    taken.remove(piece)
    removeFromBoth(piece, fieldsMap, piecesMap)
  }

  def removePieces(pieces: Traversable[Piece]) {
    pieces.foreach(removePiece(_))
  }

  def clear() {
    piecesMap.clear()
    fieldsMap.clear()
    taken.clear()
  }

  override def isEmpty: Boolean = piecesMap.isEmpty && taken.isEmpty

  def iterator: Iterator[(Field, Piece)] = piecesMap.iterator

  def processAction(move: MoveLike) {
    require(validMove(move))

    movePiece(move.piece, move.from, move.to)
  }

  def reverseAction(move: MoveLike) {
    require(validMove(move, true))

    movePiece(move.piece, move.to, move.from)
  }

  def processAction(promo: PromotionLike) {
    require(validMove(promo))

    movePiece(promo.piece, promo.from, promo.to)
    promotePiece(promo.to, promo.piece, promo.newPiece)
  }

  def reverseAction(promo: PromotionLike) {
    require(validMove(promo, true))

    revPromotePiece(promo.to, promo.piece, promo.newPiece)
    movePiece(promo.piece, promo.to, promo.from)
  }

  def processAction(capt: CaptureLike) {
    require(validCapture(capt))

    capturePiece(capt.captured, capt.capturedOn)
    movePiece(capt.piece, capt.from, capt.to)
  }

  def reverseAction(capt: CaptureLike) {
    require(validCapture(capt, true))

    movePiece(capt.piece, capt.to, capt.from)
    revCapturePiece(capt.captured, capt.capturedOn)
  }

  private def validMove(move: MoveLike, reverse: Boolean = false):
    Boolean = {

    val (from, to) =
      if (reverse) (move.to, move.from) else (move.from, move.to)
    occupiedBy(from, move.piece) && !occupied(to)
  }

  private def validCapture(capt: CaptureLike, reverse: Boolean = false):
    Boolean = {

    if (reverse) {
      !occupied(capt.from) &&
      occupiedBy(capt.to, capt.piece) &&
      taken.contains(capt.captured)
    }
    else {
      occupiedBy(capt.from, capt.piece) &&
      occupiedBy(capt.to, capt.captured) &&
      !taken.contains(capt.captured)
    }
  }

  private def validCastling(castling: Castling, reverse: Boolean = false):
    Boolean = {

    validMove(castling.kingMove, reverse) &&
    validMove(castling.rookMove, reverse)
  }

  private def movePiece(piece: Piece, from: Field, to: Field) {
    piecesMap.remove(from)
    updateBoth(to, piece)
  }

  private def promotePiece(at: Field, oldPiece: Pawn, newPiece: Piece) {
    fieldsMap.remove(oldPiece)
    updateBoth(at, newPiece)
  }

  private def revPromotePiece(at: Field, oldPiece: Pawn, newPiece: Piece) {
    fieldsMap.remove(newPiece)
    updateBoth(at, oldPiece)
  }

  private def capturePiece(captured: Piece, capturedOn: Field) {
    piecesMap.remove(capturedOn)
    fieldsMap.remove(captured)
    taken.add(captured)
  }

  private def revCapturePiece(captured: Piece, capturedOn: Field) {
    taken.remove(captured)
    updateBoth(capturedOn, captured)
  }

  private def updateBoth(field: Field, piece: Piece) {
    piecesMap(field) = piece
    fieldsMap(piece) = field
  }

  private def removeFromBoth[A, B](key1: A, map1: mutable.Map[A, B],
    map2: mutable.Map[B, A]): Option[B] = {

    map1.remove(key1) match {
      case None => None
      case Some(value) => {
        map2.remove(value)
        Some(value)
      }
    }
  }

  private val piecesMap: mutable.Map[Field, Piece] = mutable.Map()
  private val fieldsMap: mutable.Map[Piece, Field] = mutable.Map()
  private val taken: mutable.Set[Piece] = mutable.Set()
}
