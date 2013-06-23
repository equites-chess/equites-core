// Equites, a Scala chess playground
// Copyright © 2011-2013 Frank S. Thomas <frank@timepit.eu>
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
import util.Notation._
import util.Rand._

trait SquareInstances {
  implicit object squareInstance extends Equal[Square] with Order[Square] {
    // Equal
    override def equal(s1: Square, s2: Square): Boolean = s1 == s2
    // Order
    def order(s1: Square, s2: Square): Ordering = {
      val ord = Order[Int].order(s1.rank, s2.rank)
      if (ord == Ordering.EQ) Order[Int].order(s1.file, s2.file) else ord
    }
  }

  implicit val scalaOrdering = Order[Square].toScalaOrdering
}

object Square extends SquareInstances {
  def apply(algebraicFile: Char, algebraicRank: Int): Square =
    Square(algebraicFileRange.indexOf(algebraicFile),
           algebraicRankRange.indexOf(algebraicRank))

  def validCoordinates(file: Int, rank: Int): Boolean = {
    Rules.fileRange.contains(file) &&
    Rules.rankRange.contains(rank)
  }

  def validSum(that: Square, vec: Vec): Boolean =
    validCoordinates(that.file + vec.file, that.rank + vec.rank)

  def l1Dist(p: Square, q: Square): Int = p.l1Dist(q)
  def lInfDist(p: Square, q: Square): Int = p.lInfDist(q)

  def random: Rand[Square] = State(rnd => (rnd,
    Square(Rules.fileRange.start + rnd.nextInt(Rules.fileRange.length),
           Rules.rankRange.start + rnd.nextInt(Rules.rankRange.length))))

  def randomImpure(): Square = eval(random)
}

case class Square(file: Int, rank: Int) {
  def + (vec: Vec): Square = Square(file + vec.file, rank + vec.rank)
  def - (vec: Vec): Square = Square(file - vec.file, rank - vec.rank)

  def + (that: Square): Vec = Vec(file + that.file, rank + that.rank)
  def - (that: Square): Vec = Vec(file - that.file, rank - that.rank)

  def isValid: Boolean = Square.validCoordinates(file, rank)
  def asOption: Option[Square] = if (isValid) Some(this) else None

  def isLight: Boolean = isOdd(sum)
  def isDark: Boolean = isEven(sum)

  def l1Dist(that: Square): Int = (this - that).l1Length
  def lInfDist(that: Square): Int = (this - that).lInfLength

  def distToBoundary: Int = math.min(
    math.min(file - Rules.fileRange.start, Rules.fileRange.end - file),
    math.min(rank - Rules.rankRange.start, Rules.rankRange.end - rank))

  def up: Square   = this + Vec.front
  def down: Square = this + Vec.back

  def right: Square = this + Vec.right
  def left: Square  = this + Vec.left

  def rightmost: Square = Square(Rules.fileRange.end, rank)
  def leftmost: Square = Square(Rules.fileRange.start, rank)

  def toSeq: Seq[Int] = Seq(file, rank)

  private def sum = file + rank
}
