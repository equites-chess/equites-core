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

sealed trait Piece {
  def color: Color

  def isFriendOf(other: Piece): Boolean = color == other.color
  def isOpponentOf(other: Piece): Boolean = color != other.color
}

case class King  (color: Color) extends Piece
case class Queen (color: Color) extends Piece
case class Rook  (color: Color) extends Piece
case class Bishop(color: Color) extends Piece
case class Knight(color: Color) extends Piece
case class Pawn  (color: Color) extends Piece
