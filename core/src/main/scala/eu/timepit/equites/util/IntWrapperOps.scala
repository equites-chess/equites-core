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
package util

trait IntWrapperOps[T <: IntWrapperOps[T]] {
  self: T =>

  def point(i: Int): T
  def value: Int

  def apply1(f: Int => Int): T =
    point(f(value))

  def apply2(f: (Int, Int) => Int): T => T =
    t => point(f(value, t.value))

  def +(i: Int): T = apply1(_ + i)
  def -(i: Int): T = apply1(_ - i)

  def +(that: T): T = apply2(_ + _)(that)
  def -(that: T): T = apply2(_ - _)(that)

  def unary_- : T = apply1(-_)
}
