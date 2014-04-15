// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
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
package util

object BoardUtil {
  def readFenPlacement(placement: String): Board = {
    def expandDigits(target: String): String =
      """\d""".r.replaceAllIn(target, "1" * _.toString.toInt)

    val mapping = for {
      (rankStr, rank) <- placement.split("/").reverse.zipWithIndex
      (pieceChar, file) <- expandDigits(rankStr).zipWithIndex
      piece <- PieceUtil.readLetter(pieceChar)
    } yield Square(file, rank) -> piece
    Board(mapping.toMap)
  }

  /**
   * Returns the piece placement of the given [[Board]] in
   * [[http://en.wikipedia.org/wiki/Forsyth–Edwards_Notation Forsyth–Edwards Notation (FEN)]].
   */
  def showFenPlacement(board: Board): String = {
    def replaceOnes(target: String): String =
      "1{2,}".r.replaceAllIn(target, _.toString.length.toString)

    Rules.rankRange.reverse.map { rank =>
      val wholeRank = Rules.fileRange.map { file =>
        val square = Square(file, rank)
        val pieceOpt = board.get(square)
        pieceOpt.fold("1")(PieceUtil.showLetter)
      }.mkString
      replaceOnes(wholeRank)
    }.mkString("/")
  }
}
