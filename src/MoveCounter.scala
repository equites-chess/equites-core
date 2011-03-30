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

class MoveCounter extends ActionListener {
  def totalMoves(piece: Piece): Int = {
    require(count contains piece)
    count(piece)
  }

  def hasMoved(piece: Piece): Boolean = {
    require(count contains piece)
    count(piece) > 0
  }

  def addPiece(piece: Piece): Option[Int] = count.put(piece, 0)
  def removePiece(piece: Piece): Option[Int] = count.remove(piece)

  def addPieces(pieces: Traversable[Piece]) {
    pieces.foreach(addPiece(_))
  }

  def removePieces(pieces: Traversable[Piece]) {
    pieces.foreach(removePiece(_))
  }

  def clear() {
    count.clear()
  }

  def processAction(move: MoveLike) {
    incr(move.piece)
  }

  def reverseAction(move: MoveLike) {
    decr(move.piece)
  }

  def processAction(promo: PromotionLike) {
    incr(promo.piece)
    addPiece(promo.newPiece)
  }

  def reverseAction(promo: PromotionLike) {
    removePiece(promo.newPiece)
    decr(promo.piece)
  }

  def processAction(castling: Castling) {
    processAction(castling.kingMove)
  }

  def reverseAction(castling: Castling) {
    reverseAction(castling.kingMove)
  }

  private def changeCount(piece: Piece, by: Int): Int = {
    require(count contains piece)
    val result = count(piece) + by
    require(result >= 0)

    count(piece) = result
    result
  }

  private def incr(piece: Piece): Int = changeCount(piece,  1)
  private def decr(piece: Piece): Int = changeCount(piece, -1)

  private val count: mutable.Map[Piece, Int] = mutable.Map()
}
