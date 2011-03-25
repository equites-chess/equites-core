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

class Board {
  def contains(piece: Piece): Boolean = {
    grid.values.exists(_ == piece) || taken.contains(piece)
  }

  def hasMoved(piece: Piece): Boolean = counter.hasMoved(piece)
  def totalMoves(piece: Piece): Int = counter.totalMoves(piece)

  def occupied(field: Field): Boolean = grid.contains(field)

  def occupiedBy(field: Field, piece: Piece): Boolean = {
    occupied(field) && grid(field) == piece
  }

  def opponentAt(field: Field, color: Color): Boolean = {
    occupied(field) && grid(field).color != color
  }

  def getPiece(field: Field): Option[Piece] = grid.get(field)

  def putPiece(field: Field, piece: Piece) {
    require(!occupied(field) && !contains(piece))

    counter.register(piece)
    grid.put(field, piece)
  }

  def putPieces(pieces: Map[Field, Piece]) {
    pieces.foreach { case (field, piece) => putPiece(field, piece) }
  }

  def removePiece(field: Field): Option[Piece] = grid.remove(field) match {
    case None => None
    case Some(piece) => {
      counter.unregister(piece)
      Some(piece)
    }
  }

  def clear() {
    grid.clear()
    taken.clear()
    counter.unregisterAll()
  }

  def executeAction(move: Move) {
    require(occupiedBy(move.from, move.piece) && !occupied(move.to))

    movePiece(move.from, move.to)
    counter.increment(move.piece)
  }

  def revertAction(move: Move) {
    require(!occupied(move.from) && occupiedBy(move.to, move.piece))

    movePiece(move.to, move.from)
    counter.decrement(move.piece)
  }

  def executeAction(capture: Capture) {
    require(occupiedBy(capture.from, capture.piece) &&
            occupiedBy(capture.to, capture.captured) &&
            Piece.opposing(capture.piece, capture.captured) &&
            !taken.contains(capture.captured))

    taken.add(capture.captured)
    movePiece(capture.from, capture.to)
    counter.increment(capture.piece)
  }

  def revertAction(capture: Capture) {
    require(!occupied(capture.from) &&
            occupiedBy(capture.to, capture.piece) &&
            Piece.opposing(capture.piece, capture.captured) &&
            taken.contains(capture.captured))

    movePiece(capture.to, capture.from)
    counter.decrement(capture.piece)
    taken.remove(capture.captured)
    grid.put(capture.to, capture.captured)
  }

  def executeAction(promo: Promotion) {
    //require(occupiedBy(promo.from) &&
    //        !occupied(promo.to))
  }

  private def movePiece(from: Field, to: Field) {
    grid.put(to, grid.remove(from).get)
  }

  private val grid = mutable.Map[Field, Piece]()
  private val taken = mutable.Set[Piece]()
  private val counter = new MoveCounter
}

class MoveCounter {
  def hasMoved(piece: Piece): Boolean = count(piece) > 0
  def totalMoves(piece: Piece): Int = count(piece)

  def register(piece: Piece) { count.put(piece, 0) }
  def unregister(piece: Piece) { count.remove(piece) }
  def unregisterAll() { count.clear() }

  def increment(piece: Piece): Int = addTo(piece)(+1)
  def decrement(piece: Piece): Int = addTo(piece)(-1)

  private def addTo(piece: Piece)(i: Int): Int = {
    count(piece) = count(piece) + i
    count(piece)
  }

  private val count = mutable.Map[Piece, Int]()
}
