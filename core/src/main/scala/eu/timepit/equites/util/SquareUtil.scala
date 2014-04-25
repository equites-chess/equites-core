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

import scala.collection.immutable.NumericRange

import Rules._
import util.Rand._

object SquareUtil {
  val algebraicFileRange: NumericRange[Char] = toCharRange(fileRange, 'a')
  val algebraicRankRange: Range = incrRange(rankRange, 1)

  val numericFileRange: Range = incrRange(fileRange, 1)
  def numericRankRange: Range = algebraicRankRange

  def fromAlgebraic(algebraicFile: Char, algebraicRank: Int): Option[Square] = {
    val file = algebraicFileRange.indexOf(algebraicFile)
    val rank = algebraicRankRange.indexOf(algebraicRank)
    Square.from(file, rank)
  }

  /**
   * @throws IllegalArgumentException
   */
  def unsafeFromAlgebraic(algebraicFile: Char, algebraicRank: Int): Square =
    fromAlgebraic(algebraicFile, algebraicRank).getOrElse {
      throw new IllegalArgumentException
    }

  def randomSquare: Rand[Square] =
    randElem(allSquaresSeq).map(_.get)

  def showAlgebraic(square: Square): String =
    algebraicFileRange(square.file).toString +
      algebraicRankRange(square.rank).toString

  def showNumeric(square: Square): String =
    numericFileRange(square.file).toString +
      numericRankRange(square.rank).toString
}
