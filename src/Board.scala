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

import scala.collection.mutable.Map

class Board {
//  def executeAction(action: Action)
//  def revertAction(action: Action)

/*
  def getPiece(field: Field): Option[Piece] = grid.get(field)
  def takePiece(field: Field): Option[Piece] = grid.remove(field)

  def putPiece(field: Field, piece: Piece): Option[Piece] = {
    grid.put(field, piece)
  }

  def putPieces(pieces: Map[Field, Piece]) {
    pieces.foreach { case (f, p) => putPiece(f, p) }
  }
*/

  private var grid = Map[Field, Piece]()
  private var moveCount = Map[Piece, Int]()
}
