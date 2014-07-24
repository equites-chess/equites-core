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

case class File(value: Int) extends AnyVal {
  def +(that: File): File = File(value + that.value)
  def -(that: File): File = File(value - that.value)
  def unary_- : File = File(-value)
}

case class Rank(value: Int) extends AnyVal {
  def +(that: Rank): Rank = Rank(value + that.value)
  def -(that: Rank): Rank = Rank(value - that.value)
  def unary_- : Rank = Rank(-value)
}

case class Square private (file: File, rank: Rank) {
  def +(vec: Vec): Option[Square] = Square.from(file + File(vec.file), rank + Rank(vec.rank))
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
    val fileDist = minDistToBoundaries(file.value, fileSeq.map(_.value))
    val rankDist = minDistToBoundaries(rank.value, rankSeq.map(_.value))
    math.min(fileDist, rankDist)
  }

  def up: Option[Square] = this + Vec.front
  def down: Option[Square] = this + Vec.back

  def right: Option[Square] = this + Vec.right
  def left: Option[Square] = this + Vec.left

  def upRight: Option[Square] = up.flatMap(_.right)
  def upLeft: Option[Square] = up.flatMap(_.left)

  def downRight: Option[Square] = down.flatMap(_.right)
  def downLeft: Option[Square] = down.flatMap(_.left)

  def rightmost: Square = copy(file = fileSeq.last)
  def leftmost: Square = copy(file = fileSeq.head)

  def toSeq: Seq[Int] = Seq(file.value, rank.value)

  private def isValid: Boolean =
    fileSeq.contains(file) && rankSeq.contains(rank)

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

  def bottomRight: Square = Square(fileSeq.last, rankSeq.head)
  def bottomLeft: Square = Square(fileSeq.head, rankSeq.head)

  def topRight: Square = Square(fileSeq.last, rankSeq.last)
  def topLeft: Square = Square(fileSeq.head, rankSeq.last)
}

trait SquareInstances {
  implicit val squareEqual = Equal.equalA[Square]
  implicit val squareOrder = Order.order[Square] {
    case (s1, s2) =>
      val rankOrd = s1.rank.value cmp s2.rank.value
      val fileOrd = s1.file.value cmp s2.file.value
      rankOrd mappend fileOrd
  }
  implicit val squareScalaOrdering = Order[Square].toScalaOrdering
  implicit val squareShow = Show.showFromToString[Square]
}
