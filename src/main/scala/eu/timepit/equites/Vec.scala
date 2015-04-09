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

import util.Math._
import util.PlayerPerspective

case class Vec(file: Int, rank: Int) extends PlayerPerspective[Vec] {
  def map(f: Int => Int): Vec = Vec(f(file), f(rank))

  def +(that: Vec): Vec = Vec(file + that.file, rank + that.rank)
  def -(that: Vec): Vec = this + -that
  def unary_- : Vec = this * -1
  def inverse: Vec = unary_-

  def *(n: Int): Vec = map(_ * n)
  def /(n: Int): Vec = map(_ / n)

  /** Returns the greatest value of this `Vec`'s components. */
  def max: Int = math.max(file, rank)

  /** Returns the smallest value of this `Vec`'s components. */
  def min: Int = math.min(file, rank)

  /** Returns the sum of this `Vec`'s components. */
  def sum: Int = file + rank

  /**
   * Returns the [[http://en.wikipedia.org/wiki/Manhattan_distance L<sub>1</sub> length]]
   * of this `Vec`.
   */
  def l1Length: Int = map(_.abs).sum

  /**
   * Returns the [[http://en.wikipedia.org/wiki/Chebyshev_distance L<sub>∞</sub> length]]
   * of this `Vec`.
   */
  def lInfLength: Int = map(_.abs).max

  def reduced: Vec = this / positiveGcd(file, rank)

  /** Returns a copy of this `Vec` where the rank is set to zero. */
  def fileProj: Vec = copy(rank = 0)

  /** Returns a copy of this `Vec` where the file is set to zero. */
  def rankProj: Vec = copy(file = 0)

  /** Returns true if both of this `Vec`'s components are zero. */
  def isZero: Boolean = this == Vec.zero

  /** Returns true if at least one of this `Vec`'s components is non-zero. */
  def notZero: Boolean = !isZero

  def isHorizontal: Boolean = file != 0 && rank == 0
  def isVertical: Boolean = file == 0 && rank != 0

  /** Returns true if only one of this `Vec`'s components is zero. */
  def isStraight: Boolean = isHorizontal || isVertical

  def isDiagonal: Boolean = notZero && (file.abs == rank.abs)

  def rectangleTo(that: Vec): Seq[Vec] =
    for {
      f <- file to that.file
      r <- rank to that.rank
    } yield Vec(f, r)
}

object Vec extends VecInstances {
  val zero: Vec = Monoid[Vec].zero

  val front: Vec = Vec(0, 1)
  val right: Vec = Vec(1, 0)
  val back: Vec = -front
  val left: Vec = -right

  val frontRight: Vec = front + right
  val backRight: Vec = back + right
  val backLeft: Vec = back + left
  val frontLeft: Vec = front + left
}

trait VecInstances {
  implicit val vecEqual = Equal.equalA[Vec]
  implicit val vecMonoid = Monoid.instance[Vec](_ + _, Vec(0, 0))
  implicit val vecShow = Show.showFromToString[Vec]
}
