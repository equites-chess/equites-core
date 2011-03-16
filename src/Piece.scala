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

abstract class Piece {
  def color: Color
}

case class King(color: Color) extends Piece
case class Queen(color: Color) extends Piece
case class Rook(color: Color) extends Piece
case class Bishop(color: Color) extends Piece
case class Knight(color: Color) extends Piece
case class Pawn(color: Color) extends Piece
