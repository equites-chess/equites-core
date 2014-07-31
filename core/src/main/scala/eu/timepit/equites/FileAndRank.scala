// Equites, a Scala chess playground
// Copyright © 2014 Frank S. Thomas <frank@timepit.eu>
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

case class File(value: Int) extends FileAndRankOps[File] {
  def companion = File
}

case class Rank(value: Int) extends FileAndRankOps[Rank] {
  def companion = Rank
}

object File extends {
  val min: File = new File(0)
  val max: File = new File(7)
} with FileAndRankCompanion[File]

object Rank extends {
  val min: Rank = new Rank(0)
  val max: Rank = new Rank(7)
} with FileAndRankCompanion[Rank]

trait FileAndRankOps[T <: FileAndRankOps[T]] {
  self: T =>

  def value: Int
  def companion: FileAndRankCompanion[T]

  def isValid: Boolean =
    companion.min.value <= value && value <= companion.max.value

  def minDistToBounds: Int =
    math.min(value - companion.min.value, companion.max.value - value)

  def apply1(f: Int => Int): T =
    companion(f(value))

  def apply2(f: (Int, Int) => Int): T => T =
    t => companion(f(value, t.value))

  def +(i: Int): T = apply1(_ + i)
  def -(i: Int): T = apply1(_ - i)

  def +(that: T): T = apply2(_ + _)(that)
  def -(that: T): T = apply2(_ - _)(that)

  def unary_- : T = apply1(-_)
}

trait FileAndRankCompanion[T <: FileAndRankOps[T]] {
  def apply(i: Int): T

  def min: T
  def max: T

  val range: Range = min.value to max.value
  val all: Seq[T] = range.map(apply)

  implicit val equalInst = Equal.equalA[T]
  implicit val orderInst = Order.orderBy((t: T) => t.value)
}
