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
package util

import org.specs2.mutable._

import Math._

class MathSpec extends Specification {
  "isEven and isOdd" should {
    "be correct for positive numbers" in {
      isEven(4) must beTrue
      isOdd(5) must beTrue

      isEven(5) must beFalse
      isOdd(6) must beFalse
    }

    "be correct for negative numbers" in {
      isEven(-4) must beTrue
      isOdd(-5) must beTrue

      isEven(-5) must beFalse
      isOdd(-6) must beFalse
    }

    "work with Int" in {
      isEven(2) must beTrue
      isOdd(3) must beTrue
    }

    "work with Long" in {
      isEven(2: Long) must beTrue
      isOdd(3: Long) must beTrue
    }

    "work with BigInt" in {
      isEven(BigInt(2)) must beTrue
      isOdd(BigInt(3)) must beTrue
    }
  }
}
