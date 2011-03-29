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

    grid.put(field, piece)
  }

  def putPieces(pieces: Map[Field, Piece]) {
    pieces.foreach { case (field, piece) => putPiece(field, piece) }
  }

  def removePiece(field: Field): Option[Piece] = grid.remove(field) match {
    case None => None
    case Some(piece) => {
      Some(piece)
    }
  }

  def clear() {
    grid.clear()
    taken.clear()
  }

  def executeAction(move: Move) {
    require(occupiedBy(move.from, move.piece) && !occupied(move.to))

    movePiece(move.from, move.to)
  }

  def revertAction(move: Move) {
    require(!occupied(move.from) && occupiedBy(move.to, move.piece))

    movePiece(move.to, move.from)
  }

  def executeAction(capture: Capture) {
    require(occupiedBy(capture.from, capture.piece) &&
            occupiedBy(capture.to, capture.captured) &&
            Piece.opposing(capture.piece, capture.captured) &&
            !taken.contains(capture.captured))

    taken.add(capture.captured)
    movePiece(capture.from, capture.to)
  }

  def revertAction(capture: Capture) {
    require(!occupied(capture.from) &&
            occupiedBy(capture.to, capture.piece) &&
            Piece.opposing(capture.piece, capture.captured) &&
            taken.contains(capture.captured))

    movePiece(capture.to, capture.from)
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
}
