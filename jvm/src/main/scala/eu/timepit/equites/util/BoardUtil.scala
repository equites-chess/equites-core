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

/**
 * @define FEN [[http://en.wikipedia.org/wiki/Forsyth–Edwards_Notation Forsyth–Edwards Notation (FEN)]]
 */
object BoardUtil {
  /**
   * Returns a `Board` from the given $FEN piece placement. An invalid placement
   * string will result in an empty or nonsensical `Board`.
   */
  def readFenPlacement(placement: String): Board = {
    def expandDigits(target: String): String =
      """\d""".r.replaceAllIn(target, "1" * _.toString().toInt)

    val mapping = for {
      (rankStr, rank) <- placement.split("/").reverse.zipWithIndex
      (pieceChar, file) <- expandDigits(rankStr).zipWithIndex
      piece <- PieceUtil.readLetter(pieceChar)
      square <- Square.from(File(file), Rank(rank))
    } yield square -> piece
    Board(mapping.toMap)
  }

  /** Returns the piece placement of the given `Board` in $FEN. */
  def showFenPlacement(board: Board): String = {
    def replaceOnes(target: String): String =
      "1{2,}".r.replaceAllIn(target, _.toString().length.toString)

    Rank.all.reverseMap { rank =>
      val wholeRank = File.all.map { file =>
        val squareOpt = Square.from(file, rank)
        val pieceOpt = squareOpt.flatMap(board.get)
        pieceOpt.fold("1")(PieceUtil.showLetter)
      }.mkString
      replaceOnes(wholeRank)
    }.mkString("/")
  }
}
