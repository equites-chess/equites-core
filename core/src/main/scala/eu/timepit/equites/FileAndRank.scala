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

case class File(value: Int) extends util.IntWrapperOps[File] {
  def point(i: Int): File = File(i)
}

case class Rank(value: Int) extends util.IntWrapperOps[Rank] {
  def point(i: Int): Rank = Rank(i)
}

trait FileAndRankCompanion[A <: util.IntWrapperOps[A]] {
  def apply(i: Int): A

  def min: A
  def max: A

  val range: Range = min.value to max.value
  val all: Seq[A] = range.map(apply)

  implicit val aOrder = Order.orderBy((a: A) => a.value)
}

object File extends {
  val min: File = new File(0)
  val max: File = new File(7)
} with FileAndRankCompanion[File]

object Rank extends {
  val min: Rank = new Rank(0)
  val max: Rank = new Rank(7)
} with FileAndRankCompanion[Rank]
