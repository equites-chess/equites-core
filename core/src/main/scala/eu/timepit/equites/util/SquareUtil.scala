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
package util

import util.Rand._

case class AlgebraicFile(value: Char) extends AnyVal
case class AlgebraicRank(value: Int) extends AnyVal

object SquareUtil {
  val algebraicFileSeq: Seq[AlgebraicFile] = File.all.map(fileToAlgebraic)
  val algebraicRankSeq: Seq[AlgebraicRank] = Rank.all.map(rankToAlgebraic)

  val numericFileSeq: Seq[Int] = File.range.map(_ + 1)
  val numericRankSeq: Seq[Int] = Rank.range.map(_ + 1)

  def fileFromAlgebraic(algebraicFile: AlgebraicFile): File =
    File(algebraicFile.value - 'a')

  def fileToAlgebraic(file: File): AlgebraicFile =
    AlgebraicFile((file.value + 'a').toChar)

  def rankFromAlgebraic(algebraicRank: AlgebraicRank): Rank =
    Rank(algebraicRank.value - 1)

  def rankToAlgebraic(rank: Rank): AlgebraicRank =
    AlgebraicRank(rank.value + 1)

  def randomSquare: Rand[Square] =
    randElem(Square.allAsSeq).map(_.get)

  def showAlgebraic(square: Square): String =
    fileToAlgebraic(square.file).value.toString +
      rankToAlgebraic(square.rank).value.toString

  def showNumeric(square: Square): String =
    numericFileSeq(square.file.value).toString +
      numericRankSeq(square.rank.value).toString
}
