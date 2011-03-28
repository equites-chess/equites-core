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

class MoveCounter() {
  def totalMoves(piece: Piece): Int = count(piece)
  def hasMoved(piece: Piece): Boolean = count(piece) > 0

  def addPiece(piece: Piece): Option[Int] = count.put(piece, 0)
  def removePiece(piece: Piece): Option[Int] = count.remove(piece)

  def addPieces(pieces: Traversable[Piece]) { pieces.foreach(addPiece(_)) }
  def removePieces() { count.clear() }

  private def changeCount(piece: Piece, by: Int): Int = {
    val result = count(piece) + by
    count(piece) = result
    result
  }

  private def incr(piece: Piece): Int = changeCount(piece,  1)
  private def decr(piece: Piece): Int = changeCount(piece, -1)

  private val count: mutable.Map[Piece, Int] = mutable.Map()
}
