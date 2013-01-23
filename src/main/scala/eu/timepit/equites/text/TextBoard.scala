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
import util.Notation._

trait AbstractRepr {
  def pieceToString(piece: Piece): String
  def tileStart: String       = ""
  def tileEmpty: String       = ""
  def tileEnd: String         = ""
  def rankBegin: String       = ""
  def rankSep: String         = ""
  def rankEnd: String         = ""
  def horizontalBar: String   = ""
  def verticalBar: String     = ""
  def corner: String          = ""
  def fileLabelsStart: String = ""
  def fileLabelsSep: String   = " "
  def fileLabelsEnd: String   = " "

  def rankLabelsRight: Boolean = true
}

trait LetterRepr extends AbstractRepr {
  def pieceToString(piece: Piece) = piece.toLetter
  override def tileEmpty   = "."
  override def rankSep     = " "
  override def verticalBar = "  "
  override def corner      = " "
}

trait FigurineRepr extends AbstractRepr {
  def pieceToString(piece: Piece) = piece.toFigurine
  override def tileEmpty     = "\u00B7"  // ·
  override def rankSep       = " "
  override def horizontalBar = "\u2500"  // ─
  override def verticalBar   = "\u2502 " // │
  override def corner        = "\u2518"  // ┘
}

trait WikiRepr extends AbstractRepr {
  def pieceToString(piece: Piece) = piece.toWikiLetters
  override def tileStart       = "|"
  override def tileEmpty       = "  "
  override def tileEnd         = ""
  override def rankEnd         = "|="
  override def verticalBar     = " "
  override def fileLabelsStart = "   "
  override def fileLabelsSep   = "  "
  override def fileLabelsEnd   = " "
  override def rankLabelsRight = false
}

trait TextBoard {
  self: AbstractRepr =>

  def mkUnlabeled(board: Board): String = {
    def squareToString(square: Square): String =
      board.get(square).map(pieceToString).getOrElse(tileEmpty).mkString(
        tileStart, "", tileEnd)

    def rowToString(rank: Int): String =
      Rules.rankSquares(rank).map(squareToString).mkString(
        rankBegin, rankSep, rankEnd + "\n")

    Rules.rankRange.reverse.map(rowToString).mkString
  }

  def mkLabeled(board: Board): String = {
    def addRankLabel(r: Int): String =
      if (rankLabelsRight) s"${verticalBar}${r}" else s"${r}${verticalBar}"

    def boardWithRankLabels: String = {
      val lines = mkUnlabeled(board).split("\n").toSeq
      val labels = algebraicRankRange.reverse.map(addRankLabel)
      val zipped =
        if (rankLabelsRight) lines.zip(labels) else labels.zip(lines)

      zipped.map(_.productIterator.mkString("", "", "\n")).mkString
    }

    def bottomBorder: String = {
      val barWidth = Rules.fileRange.length * 2 - 1
      val border = (horizontalBar * barWidth) + corner
      if (border.isEmpty()) "" else border + "\n"
    }

    boardWithRankLabels + bottomBorder + algebraicFileRange.mkString(
      fileLabelsStart, fileLabelsSep, fileLabelsEnd + "\n")
  }
}
