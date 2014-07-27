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

import util.Math._

case class Square private (file: File, rank: Rank) {
  def +(vec: Vec): Option[Square] = Square.from(file + vec.file, rank + vec.rank)
  def -(vec: Vec): Option[Square] = this + -vec

  def +(that: Square): Vec = Vec((file + that.file).value, (rank + that.rank).value)
  def -(that: Square): Vec = this + -that

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

  def to(that: Square): Draw = Draw(this, that)

  def distToBoundary: Int = {
    val fileDist = minDistToBoundaries(file.value, File.range)
    val rankDist = minDistToBoundaries(rank.value, Rank.range)
    math.min(fileDist, rankDist)
  }

  def up: Option[Square] = this + Vec.front
  def down: Option[Square] = this + Vec.back

  def right: Option[Square] = this + Vec.right
  def left: Option[Square] = this + Vec.left

  def upRight: Option[Square] = this + Vec.frontRight
  def upLeft: Option[Square] = this + Vec.frontLeft

  def downRight: Option[Square] = this + Vec.backRight
  def downLeft: Option[Square] = this + Vec.backLeft

  def rightmost: Square = copy(file = File.max)
  def leftmost: Square = copy(file = File.min)

  def toSeq: Seq[Int] = Seq(file.value, rank.value)

  private def isValid: Boolean =
    File.range.contains(file.value) && Rank.range.contains(rank.value)

  /** Returns `Some(this)` if this `Square` is valid and `None` otherwise. */
  private def asOption: Option[Square] = isValid.option(this)

  private def unary_- : Square = Square(-file, -rank)

  private def sum: Int = file.value + rank.value
}

object Square extends SquareInstances {
  def from(file: File, rank: Rank): Option[Square] =
    Square(file, rank).asOption

  /**
   * @throws IllegalArgumentException
   */
  def unsafeFrom(file: File, rank: Rank): Square =
    from(file, rank).getOrElse(throw new IllegalArgumentException)

  def allWithFile(file: File): Seq[Square] = Rank.all.flatMap(Square.from(file, _))
  def allWithRank(rank: Rank): Seq[Square] = File.all.flatMap(Square.from(_, rank))

  val allAsSeq: Seq[Square] = File.all.flatMap(allWithFile)
  val allAsSet: Set[Square] = allAsSeq.toSet

  val bottomRight: Square = Square(File.max, Rank.min)
  val bottomLeft: Square = Square(File.min, Rank.min)

  val topRight: Square = Square(File.max, Rank.max)
  val topLeft: Square = Square(File.min, Rank.max)
}

trait SquareInstances {
  implicit val squareEqual = Equal.equalA[Square]
  implicit val squareOrder = Order.order[Square] {
    case (s1, s2) => (s1.rank cmp s2.rank) mappend (s1.file cmp s2.file)
  }
  implicit val squareScalaOrdering = Order[Square].toScalaOrdering
  implicit val squareShow = Show.showFromToString[Square]
}
