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

object Rules {
  def startingPositions(color: Color): Map[Field, Piece] = {
    val backRank = Color.selectBy(color, 0, 7)
    val pawnRank = Color.selectBy(color, 1, 6)

    val royals = List(Field(4, backRank) -> King(color),
                      Field(3, backRank) -> Queen(color))
    val rooks   = List(0, 7).map(Field(_, backRank) -> Rook(color))
    val knights = List(1, 6).map(Field(_, backRank) -> Knight(color))
    val bishops = List(2, 5).map(Field(_, backRank) -> Bishop(color))
    val pawns = (0 to 7).map(Field(_, pawnRank) -> Pawn(color))

    Map[Field, Piece]() ++ royals ++ rooks ++ knights ++ bishops ++ pawns
  }

  def startingPositions: Map[Field, Piece] = {
     startingPositions(White) ++ startingPositions(Black)
  }
}

class Rules
