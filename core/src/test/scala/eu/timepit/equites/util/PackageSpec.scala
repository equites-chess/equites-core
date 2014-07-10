// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import org.specs2._
import scalaz.std.option._
import scalaz.std.stream._

class PackageSpec extends Specification {
  def is = s2"""
  util.backtrack should
    generate all 'binary' strings of length three     $e1
    generate all odd 'binary' strings of length three $e2
    count to ten with Stream and Option               $e3
  """

  def nextBinary(c: String): Stream[String] = Stream("0", "1").map(_ + c)

  def e1 = backtrack("")(nextBinary, _.length == 3).toSet must_==
    Set("000", "001", "010", "011", "100", "101", "110", "111")

  def e2 = backtrack("1")(nextBinary, _.length == 3).toSet must_==
    Set("001", "011", "101", "111")

  def e3 =
    (backtrack(0)(i => Stream(i + 1), _ == 10) must_== Stream(10)) and
      (backtrack(0)(i => Option(i + 1), _ == 10) must_== Option(10))
}
