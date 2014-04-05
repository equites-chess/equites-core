// Equites, a Scala chess playground
// Copyright © 2011, 2013 Frank S. Thomas <frank@timepit.eu>
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

import scala.annotation.tailrec

object Math {
  /** Returns the greatest common divisor of `x` and `y`. */
  @tailrec
  def gcd[I](x: I, y: I)(implicit I: Integral[I]): I =
    if (y == 0) x else gcd(y, I.rem(x, y))

  /** Returns true if `i` is divisible by two.*/
  def isEven[I: Integral](i: I): Boolean = remBy2(i) == 0

  /** Returns true if `i` is not divisible by two. */
  def isOdd[I: Integral](i: I): Boolean = remBy2(i) != 0

  /** Returns the remainder of `i` divided by two. */
  private def remBy2[I](i: I)(implicit I: Integral[I]): I =
    I.rem(i, I.fromInt(2))

  def minDistToEndpoints(x: Int, range: Range): Int =
    math.min(x - range.start, range.end - x)
}
