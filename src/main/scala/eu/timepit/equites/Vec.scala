// Equites, a simple chess interface
// Copyright © 2011-2013 Frank S. Thomas <f.thomas@gmx.de>
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

import scalaz.Monoid

trait VecInstances {
  implicit val vecAddition = new Monoid[Vec] {
    def zero: Vec = Vec(0, 0)
    def append(s1: Vec, s2: => Vec): Vec = s1 + s2
  }
}

case class Vec(file: Int, rank: Int) {
  def map(f: Int => Int): Vec = Vec(f(file), f(rank))

  def +(that: Vec): Vec = Vec(file + that.file, rank + that.rank)
  def -(that: Vec): Vec = this + -that
  def unary_- : Vec = Vec(-file, -rank)

  def *(n: Int): Vec = map(_ * n)
  def /(n: Int): Vec = map(_ / n)

  def max: Int = math.max(file, rank)
  def min: Int = math.min(file, rank)
  def sum: Int = file + rank

  def l1Length: Int = map(_.abs).sum
  def lInfLength: Int = map(_.abs).max
}
