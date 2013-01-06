// Equites, a simple chess interface
// Copyright © 2011, 2013 Frank S. Thomas <f.thomas@gmx.de>
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
package text

import implicits.RichPieceImplicit._
import utils.Notation._

trait Letters {
  def pieceToString(piece: Piece): String = piece.toLetter
  def emptyTile: String     = "."
  def horizontalBar: String = ""
  def verticalBar: String   = " "
  def corner: String        = ""
}

trait Figurine {
  def pieceToString(piece: Piece): String = piece.toFigurine
  def emptyTile: String     = "\u00B7" // ·
  def horizontalBar: String = "\u2500" // ─
  def verticalBar: String   = "\u2502" // │
  def corner: String        = "\u2518" // ┘
}

abstract class TextBoard {
  def mkUnlabeled(board: Board): String = {
    def squareToString(square: Square): String =
      board.get(square).map(pieceToString).getOrElse(emptyTile)

    def rowToString(rank: Int): String =
      Rules.rankSquares(rank).map(squareToString).mkString("", " ", " \n")

    Rules.rankRange.reverse.map(rowToString).mkString
  }

  def mkLabeled(board: Board): String = {
    def boardWithRankLabels: String = {
      val lines = mkUnlabeled(board).split(" \n")
      val labels = algebraicRankRange.reverse.map(r => s"${verticalBar} ${r}\n")
      lines.zip(labels).map(_.productIterator.mkString).mkString
    }

    def bottomBorder: String = {
      val barWidth = Rules.fileRange.length * 2 - 1
      (horizontalBar * barWidth) + corner + "\n"
    }

    boardWithRankLabels + bottomBorder +
      algebraicFileRange.mkString("", " ", " \n")
  }

  protected def pieceToString(piece: Piece): String
  protected def emptyTile: String
  protected def horizontalBar: String
  protected def verticalBar: String
  protected def corner: String
}
