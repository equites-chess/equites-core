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
import Scalaz._

import Rules._
import util.Math._
import util.Notation._
import util.Rand._

trait SquareInstances {
  implicit val squareInstance = new Order[Square] {
    // Order
    def order(s1: Square, s2: Square): Ordering =
      (s1.rank cmp s2.rank) mappend (s1.file cmp s2.file)
  }

  implicit val squareEqual = Equal.equalA[Square]
  implicit val squareScalaOrdering = Order[Square].toScalaOrdering
  implicit val squareShow = Show.showFromToString[Square]
}

object Square extends SquareInstances {
  def apply(algebraicFile: Char, algebraicRank: Int): Square = {
    val file = algebraicFileRange.indexOf(algebraicFile)
    val rank = algebraicRankRange.indexOf(algebraicRank)
    Square(file, rank)
  }

  def random: Rand[Square] =
    for {
      file <- randomRangeElem(fileRange)
      rank <- randomRangeElem(rankRange)
    } yield Square(file, rank)

  def randomImpure(): Square = eval(random)

  def topRight: Square = Square(fileRange.end, rankRange.end)
  def topLeft: Square = Square(fileRange.start, rankRange.end)
}

case class Square(file: Int, rank: Int) {
  def +(vec: Vec): Square = Square(file + vec.file, rank + vec.rank)
  def -(vec: Vec): Square = this + -vec

  def +(that: Square): Vec = Vec(file + that.file, rank + that.rank)
  def -(that: Square): Vec = Vec(file - that.file, rank - that.rank)

  def isValid: Boolean = fileRange.contains(file) && rankRange.contains(rank)
  def asOption: Option[Square] = isValid.option(this)

  def isLight: Boolean = isOdd(sum)
  def isDark: Boolean = isEven(sum)

  def l1Dist(that: Square): Int = (this - that).l1Length
  def lInfDist(that: Square): Int = (this - that).lInfLength

  def distToBoundary: Int = {
    val fileDist = minDistToEndpoints(file, fileRange)
    val rankDist = minDistToEndpoints(rank, rankRange)
    math.min(fileDist, rankDist)
  }

  def up: Square = this + Vec.front
  def down: Square = this + Vec.back

  def right: Square = this + Vec.right
  def left: Square = this + Vec.left

  def rightmost: Square = copy(file = fileRange.end)
  def leftmost: Square = copy(file = fileRange.start)

  def toSeq: Seq[Int] = Seq(file, rank)

  private[this] def sum = file + rank
}
