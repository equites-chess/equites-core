// Equites, a Scala chess playground
// Copyright © 2011-2014 Frank S. Thomas <frank@timepit.eu>
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

import scalaz._
import scalaz.Scalaz._

import Rules._
import util.Math._

case class Square private (file: Int, rank: Int) {
  def +(vec: Vec): Option[Square] = Square.from(file + vec.file, rank + vec.rank)
  def -(vec: Vec): Option[Square] = this + -vec

  def +(that: Square): Vec = Vec(file + that.file, rank + that.rank)
  def -(that: Square): Vec = this + -that

  def sum: Int = file + rank

  /** Returns true if this `Square` is light. */
  def isLight: Boolean = isOdd(sum)

  /** Returns true if this `Square` is dark. */
  def isDark: Boolean = isEven(sum)

  /**
   * Returns the [[http://en.wikipedia.org/wiki/Manhattan_distance L<sub>1</sub> distance]]
   * of this `Square` to `that`.
   */
  def l1Dist(that: Square): Int = (this - that).l1Length

  /**
   * Returns the [[http://en.wikipedia.org/wiki/Chebyshev_distance L<sub>∞</sub> distance]]
   * of this `Square` to `that`.
   */
  def lInfDist(that: Square): Int = (this - that).lInfLength

  def isAdjacent(that: Square): Boolean = lInfDist(that) == 1

  /** Returns true if this `Square` is on the same diagonal as `that`. */
  def isSameDiagonal(that: Square): Boolean = (this - that).isDiagonal

  /** Returns true if this `Square` is on the same file or rank as `that`. */
  def isSameLine(that: Square): Boolean = (this - that).isStraight

  def distToBoundary: Int = {
    val fileDist = minDistToBoundaries(file, fileRange)
    val rankDist = minDistToBoundaries(rank, rankRange)
    math.min(fileDist, rankDist)
  }

  def up: Option[Square] = this + Vec.front
  def down: Option[Square] = this + Vec.back

  def right: Option[Square] = this + Vec.right
  def left: Option[Square] = this + Vec.left

  def rightmost: Square = copy(file = fileRange.end)
  def leftmost: Square = copy(file = fileRange.start)

  def toSeq: Seq[Int] = Seq(file, rank)

  private def isValid: Boolean =
    fileRange.contains(file) && rankRange.contains(rank)

  /** Returns `Some(this)` if this `Square` is valid and `None` otherwise. */
  private def asOption: Option[Square] = isValid.option(this)

  private def unary_- : Square = Square(-file, -rank)
}

object Square extends SquareInstances {
  def from(file: Int, rank: Int): Option[Square] =
    Square(file, rank).asOption

  /**
   * @throws IllegalArgumentException
   */
  def unsafeFrom(file: Int, rank: Int): Square =
    from(file, rank).getOrElse(throw new IllegalArgumentException)

  def bottomRight: Square = Square(fileRange.end, rankRange.start)
  def bottomLeft: Square = Square(fileRange.start, rankRange.start)

  def topRight: Square = Square(fileRange.end, rankRange.end)
  def topLeft: Square = Square(fileRange.start, rankRange.end)
}

trait SquareInstances {
  implicit val squareEqual = Equal.equalA[Square]
  implicit val squareOrder = Order.order[Square] {
    case (s1, s2) => (s1.rank cmp s2.rank) mappend (s1.file cmp s2.file)
  }
  implicit val squareScalaOrdering = Order[Square].toScalaOrdering
  implicit val squareShow = Show.showFromToString[Square]
}
