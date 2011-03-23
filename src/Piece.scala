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

object Piece {
  def opposing(piece1: Piece, piece2: Piece): Boolean = {
    piece1.color != piece2.color
  }
}

sealed abstract class Piece {
  def color: Color
}

class King(val color: Color) extends Piece
class Queen(val color: Color) extends Piece
class Rook(val color: Color) extends Piece
class Bishop(val color: Color) extends Piece
class Knight(val color: Color) extends Piece
class Pawn(val color: Color) extends Piece

trait PieceCompanion {
  def unapply(piece: Piece): Option[Color] = Some(piece.color)
}

object King extends PieceCompanion
object Queen extends PieceCompanion
object Rook extends PieceCompanion
object Bishop extends PieceCompanion
object Knight extends PieceCompanion
object Pawn extends PieceCompanion
