// Equites, a simple chess interface
// Copyright © 2013 Frank S. Thomas <f.thomas@gmx.de>
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

import org.specs2.mutable._

import GenericImplicits._

class GenericImplicitsSpec extends Specification {
  "asOption" should {
    "return None on empty collections" in {
      Array[Int]().asOption must beNone
      List[Int]().asOption must beNone
      "".asOption must beNone
    }
    "return Some(...) on non-empty collections" in {
      List(1,2,3).asOption must beSome(List(1,2,3))
      "Hello".asOption must beSome("Hello")
    }
  }

  "dropLeftRight" should {
    "work on empty collections" in {
      "".dropLeftRight(1) must_== ""
      List().dropLeftRight(1) must_== List()
    }
    "work on non-empty collections" in {
      "12345".dropLeftRight(2) must_== "3"
      List(1,2,3,4).dropLeftRight(1) must_== List(2,3)
    }
  }
}
