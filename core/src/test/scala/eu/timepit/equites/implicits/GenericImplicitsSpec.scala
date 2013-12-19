// Equites, a Scala chess playground
// Copyright © 2013 Frank S. Thomas <frank@timepit.eu>
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
package implicits

import org.specs2._
import scalaz._
import Scalaz._

import GenericImplicits._

class GenericImplicitsSpec extends Specification { def is = s2"""
  asOption should
    return None on empty collections          $e1
    return Some(x) on non-empty collections x $e2

  dropLeftRight should
    be the identity on empty collections   $e3
    drop elements on non-empty collections $e4
  """

  def e1 =
    (List.empty[Int].asOption must beNone) and
    ("".asOption must beNone)

  def e2 =
    (List(1, 2, 3).asOption must beSome(List(1, 2, 3))) and
    ("Hello".asOption must beSome("Hello"))

  def e3 =
    (List.empty[Int].dropLeftRight(1) must_== List.empty[Int]) and
    ("".dropLeftRight(1) must_== "")

  def e4 =
    (List(1, 2, 3, 4).dropLeftRight(1) must_== List(2, 3)) and
    ("1234".dropLeftRight(1) must_== "23")
}
