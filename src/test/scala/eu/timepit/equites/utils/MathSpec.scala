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
package utils

import org.specs2.mutable._

class MathSpec extends Specification {
  "isEven and isOdd" should {
    "be correct for positive numbers" in {
      Math.isEven(4) must beTrue
      Math.isOdd(5) must beTrue

      Math.isEven(5) must beFalse
      Math.isOdd(6) must beFalse
    }

    "be correct for negative numbers" in {
      Math.isEven(-4) must beTrue
      Math.isOdd(-5) must beTrue

      Math.isEven(-5) must beFalse
      Math.isOdd(-6) must beFalse
    }

    "work with Int" in {
      Math.isEven(2) must beTrue
      Math.isOdd(3) must beTrue
    }

    "work with Long" in {
      Math.isEven(2: Long) must beTrue
      Math.isOdd(3: Long) must beTrue
    }

    "work with BigInt" in {
      Math.isEven(BigInt(2)) must beTrue
      Math.isOdd(BigInt(3)) must beTrue
    }
  }
}
