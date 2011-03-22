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
  val maxFile = 7
  val maxRank = 7

  val kingFile  = 4
  val queenFile = 3

  val rookFiles   = List(0, 7)
  val knightFiles = List(1, 6)
  val bishopFiles = List(2, 5)

  val castlingShortFile = 6
  val castlingLongFile  = 3

  def backRankBy(color: Color): Int = if (color == White) 0 else maxRank
  def pawnRankBy(color: Color): Int = if (color == White) 1 else maxRank - 1

  def startingPositions(color: Color): Map[Field, Piece] = {
    val backRank = backRankBy(color)
    val pawnRank = pawnRankBy(color)

    val royals = List(Field(kingFile,  backRank) -> King(color),
                      Field(queenFile, backRank) -> Queen(color))
    val rooks   =      rookFiles.map(Field(_, backRank) -> Rook(color))
    val knights =    knightFiles.map(Field(_, backRank) -> Knight(color))
    val bishops =    bishopFiles.map(Field(_, backRank) -> Bishop(color))
    val pawns   = (0 to maxFile).map(Field(_, pawnRank) -> Pawn(color))

    Map[Field, Piece]() ++ royals ++ rooks ++ knights ++ bishops ++ pawns
  }

  def startingPositions: Map[Field, Piece] = {
     startingPositions(White) ++ startingPositions(Black)
  }
}

class Rules
