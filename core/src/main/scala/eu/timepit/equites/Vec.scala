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

trait VecInstances {
  implicit object vecInstance extends Equal[Vec] with Monoid[Vec] {
    // Equal:
    def equal(v1: Vec, v2: Vec): Boolean = v1 == v2
    // Monoid:
    def zero: Vec = Vec(0, 0)
    def append(v1: Vec, v2: => Vec): Vec = v1 + v2
  }
}

object Vec extends VecInstances

case class Vec(file: Int, rank: Int) {
  def map(f: Int => Int): Vec = Vec(f(file), f(rank))

  def + (that: Vec): Vec = Vec(file + that.file, rank + that.rank)
  def - (that: Vec): Vec = this + -that
  def unary_- : Vec = this * -1

  def * (n: Int): Vec = map(_ * n)
  def / (n: Int): Vec = map(_ / n)

  def max: Int = math.max(file, rank)
  def min: Int = math.min(file, rank)
  def sum: Int = file + rank

  def l1Length: Int = map(_.abs).sum
  def lInfLength: Int = map(_.abs).max

  def reduced: Vec = this / math.max(1, gcd(file, rank).abs)

  def to(that: Vec): Seq[Vec] = for {
    f <- file to that.file
    r <- rank to that.rank
  } yield Vec(f, r)
}
