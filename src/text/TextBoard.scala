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
package text

import utils.Notation

trait Letters {
  def pieceToString(piece: Piece): String = Notation.toLetter(piece)
  val tile: String = "."
  val hBar: String = ""
  val vBar: String = " "
  val edge: String = ""
}

trait Figurine {
  def pieceToString(piece: Piece): String = Notation.toFigurine(piece)
  val tile: String = "\u00B7" // ·
  val hBar: String = "\u2500" // ─
  val vBar: String = "\u2502" // │
  val edge: String = "\u2518" // ┘
}

abstract class TextBoard(board: Board) {
  def unlabeledBoard(): String = {
    val sb = new StringBuilder
    for (rank <- Rules.rankRange.reverse) {
      for (file <- Rules.fileRange) {
        sb append fieldToString(Field(file, rank))
        sb append ' '
      }
      sb append '\n'
    }
    sb.toString
  }

  def labeledBoard(): String = {
    val sb = new StringBuilder(unlabeledBoard)
    var idx = 0

    for (rank <- Notation.algebraicRankRange.reverse) {
      val replacement = vBar + " " + rank + "\n"

      idx += (Rules.fileRange.length * 2 - 1)
      sb.replace(idx, idx + 2, replacement)
      idx += replacement.length
    }
    sb.toString + bottomBorder + fileLabels
  }

  private def fieldToString(field: Field): String = {
    board.getPiece(field) match {
      case Some(piece) => pieceToString(piece)
      case None => tile
    }
  }

  private def bottomBorder: String = {
    val barLength = Rules.fileRange.length * 2 - 1
    (hBar * barLength) + edge + "\n"
  }

  private def fileLabels: String = {
    Notation.algebraicFileRange.foldLeft("")((x, y) => x + y + " ") + "\n"
  }

  protected def pieceToString(piece: Piece): String
  protected val tile: String
  protected val hBar: String
  protected val vBar: String
  protected val edge: String
}
