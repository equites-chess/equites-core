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
  val fileRange = 0 to 7
  val rankRange = 0 to 7

  val kingFile  = 4
  val queenFile = 3
  val rookFiles   = List(0, 7)
  val knightFiles = List(1, 6)
  val bishopFiles = List(2, 5)

  def backRankBy(color: Color): Int = {
    if (color == White) rankRange.start else rankRange.end
  }

  def pawnRankBy(color: Color): Int = {
    if (color == White) rankRange.start + 1 else rankRange.start - 1
  }

  def startingPositions(color: Color): Map[Field, Piece] = {
    val backRank = backRankBy(color)
    val pawnRank = pawnRankBy(color)

    val royals  = List(Field(kingFile,  backRank) -> new King(color),
                       Field(queenFile, backRank) -> new Queen(color))
    val rooks   =   rookFiles.map(Field(_, backRank) -> new Rook(color))
    val knights = knightFiles.map(Field(_, backRank) -> new Knight(color))
    val bishops = bishopFiles.map(Field(_, backRank) -> new Bishop(color))
    val pawns   =   fileRange.map(Field(_, pawnRank) -> new Pawn(color))

    Map[Field, Piece]() ++ royals ++ rooks ++ knights ++ bishops ++ pawns
  }

  def startingPositions: Map[Field, Piece] = {
     startingPositions(White) ++ startingPositions(Black)
  }

  def castlingFields(piece: Piece, side: Symbol): Pair[Field, Field] = {
    val rank = backRankBy(piece.color)

    piece match {
      case King(_) if side == 'kingside
        => (Field(kingFile, rank), Field(kingFile + 2, rank))
      case King(_) if side == 'queenside
        => (Field(kingFile, rank), Field(kingFile - 2, rank))
      case Rook(_) if side == 'kingside
        => (Field(rookFiles(1), rank), Field(kingFile + 1, rank))
      case Rook(_) if side == 'queenside
        => (Field(rookFiles(0), rank), Field(kingFile - 1, rank))
      case _ => throw new IllegalArgumentException
    }
  }
}

class Rules
