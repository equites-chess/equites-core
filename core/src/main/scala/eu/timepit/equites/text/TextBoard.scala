// Equites, a Scala chess playground
// Copyright © 2011, 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import util.PieceUtil._
import util.SquareUtil._

// format: OFF
trait AbstractTheme {
  def showPiece(piece: AnyPiece): String
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
  val rankLabels: Seq[String]  = algebraicRankSeq.map(_.value.toString)
  val fileLabels: Seq[String]  = algebraicFileSeq.map(_.value.toString)
  def lineSep: String          = "\n"
}

trait LetterTheme extends AbstractTheme {
  override def showPiece(piece: AnyPiece) = showLetter(piece)
  override def tileEmpty       = "."
  override def rankSep         = " "
  override def verticalBar     = "  "
  override def corner          = " "
}

trait FigurineTheme extends AbstractTheme {
  override def showPiece(piece: AnyPiece) = showFigurine(piece)
  override def tileEmpty       = "·"
  override def rankSep         = " "
  override def horizontalBar   = "─"
  override def verticalBar     = "│ "
  override def corner          = "┘"
}

trait WikiTheme extends AbstractTheme {
  override def showPiece(piece: AnyPiece) = showWikiLetters(piece)
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

trait NumericLabels extends AbstractTheme {
  override val rankLabels: Seq[String] = numericRankSeq.map(_.toString)
  override val fileLabels: Seq[String] = numericFileSeq.map(_.toString)
}
// format: ON

trait TextBoard {
  self: AbstractTheme =>

  def mkUnlabeled(board: Board): String = {
    def showSquare(square: Square): String =
      board.get(square).fold(tileEmpty)(showPiece)
        .mkString(tileStart, "", tileEnd)

    def showRank(rank: Rank): String =
      Rules.rankSquares(rank).map(showSquare)
        .mkString(rankBegin, rankSep, rankEnd + lineSep)

    Rules.rankSeq.reverse.map(showRank).mkString
  }

  def mkLabeled(board: Board): String = {
    val addVerticalBar: String => String =
      if (rankLabelsRight) verticalBar + _ else _ + verticalBar

    def boardWithRankLabels: String = {
      val lines = mkUnlabeled(board).split(lineSep).toSeq
      val labels = rankLabels.reverse.map(addVerticalBar)
      val zipped = if (rankLabelsRight) lines.zip(labels) else labels.zip(lines)

      zipped.map(_.productIterator.mkString + lineSep).mkString
    }

    def bottomBorder: String = {
      val barWidth = Rules.fileSeq.length * 2 - 1
      val border = (horizontalBar * barWidth) + corner
      if (border.isEmpty) "" else border + lineSep
    }

    def fileLabelsLine: String =
      fileLabels.mkString(fileLabelsStart, fileLabelsSep, fileLabelsEnd + lineSep)

    boardWithRankLabels + bottomBorder + fileLabelsLine
  }
}

object LetterTextBoard extends TextBoard with LetterTheme
object FigurineTextBoard extends TextBoard with FigurineTheme
object WikiTextBoard extends TextBoard with WikiTheme
