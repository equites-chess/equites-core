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
package text

import implicits.PieceImplicits._
import util.Notation._

trait AbstractRepr {
  def pieceToString(piece: AnyPiece): String
  def tileStart: String        = ""
  def tileEmpty: String
  def tileEnd: String          = ""
  def rankBegin: String        = ""
  def rankSep: String          = ""
  def rankEnd: String          = ""
  def horizontalBar: String    = ""
  def verticalBar: String
  def corner: String           = ""
  def fileLabelsStart: String  = ""
  def fileLabelsSep: String    = " "
  def fileLabelsEnd: String    = " "
  def rankLabelsRight: Boolean = true
  val rankLabels: Seq[String]  = algebraicRankRange.map(_.toString)
  val fileLabels: Seq[String]  = algebraicFileRange.map(_.toString)
}

trait LetterRepr extends AbstractRepr {
  def pieceToString(piece: AnyPiece) = piece.toLetter
  def tileEmpty        = "."
  override def rankSep = " "
  def verticalBar      = "  "
  override def corner  = " "
}

trait FigurineRepr extends AbstractRepr {
  def pieceToString(piece: AnyPiece) = piece.toFigurine
  def tileEmpty              = "\u00B7"  // ·
  override def rankSep       = " "
  override def horizontalBar = "\u2500"  // ─
  override def verticalBar   = "\u2502 " // │
  override def corner        = "\u2518"  // ┘
}

trait WikiRepr extends AbstractRepr {
  def pieceToString(piece: AnyPiece) = piece.toWikiLetters
  override def tileStart       = "|"
  def tileEmpty                = "  "
  override def tileEnd         = ""
  override def rankEnd         = "|="
  def verticalBar              = " "
  override def fileLabelsStart = "   "
  override def fileLabelsSep   = "  "
  override def fileLabelsEnd   = " "
  override def rankLabelsRight = false
}

trait NumericLabels extends AbstractRepr {
  override val rankLabels: Seq[String] = numericRankRange.map(_.toString)
  override val fileLabels: Seq[String] = numericFileRange.map(_.toString)
}

trait TextBoard {
  self: AbstractRepr =>

  def mkUnlabeled(board: Board): String = {
    def squareToString(square: Square): String =
      board.get(square).fold(tileEmpty)(pieceToString)
        .mkString(tileStart, "", tileEnd)

    def rowToString(rank: Int): String =
      Rules.rankSquares(rank).map(squareToString)
        .mkString(rankBegin, rankSep, rankEnd + "\n")

    Rules.rankRange.reverse.map(rowToString).mkString
  }

  def mkLabeled(board: Board): String = {
    def addRankLabel(r: String): String =
      if (rankLabelsRight) s"${verticalBar}${r}" else s"${r}${verticalBar}"

    def boardWithRankLabels: String = {
      val lines = mkUnlabeled(board).split("\n").toSeq
      val labels = rankLabels.reverse.map(addRankLabel)
      val zipped = if (rankLabelsRight) lines.zip(labels) else labels.zip(lines)

      zipped.map(_.productIterator.mkString("", "", "\n")).mkString
    }

    def bottomBorder: String = {
      val barWidth = Rules.fileRange.length * 2 - 1
      val border = (horizontalBar * barWidth) + corner
      if (border.isEmpty) "" else border + "\n"
    }

    def fileLabelsLine: String =
      fileLabels.mkString(fileLabelsStart, fileLabelsSep, fileLabelsEnd + "\n")

    boardWithRankLabels + bottomBorder + fileLabelsLine
  }
}

object LetterTextBoard extends TextBoard with LetterRepr
object FigurineTextBoard extends TextBoard with FigurineRepr
object WikiTextBoard extends TextBoard with WikiRepr
