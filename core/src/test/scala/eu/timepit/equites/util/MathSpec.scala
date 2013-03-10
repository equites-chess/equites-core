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

import org.specs2.ScalaCheck
import org.specs2.matcher.DataTables
import org.specs2.mutable._

import Math._

class MathSpec extends Specification with DataTables with ScalaCheck {
  "gcd" should {
    "be commutative" in check {
      (a: Int, b: Int) => {
        val (x, y) = (a.abs, b.abs)
        gcd(x, y) must_== gcd(y, x)
      }
    }
  }

  "isEven and isOdd" should {
    "be correct for positive numbers" in {
      "a" | "isEven" |
      0   ! true     |
      1   ! false    |
      2   ! true     |
      3   ! false    |
      4   ! true     |> {
        (a, r) => isEven(a) must_== r
      }
    }

    def mustBeSymmetric[A, B](f: A => B)(implicit A: Integral[A]) =
      (a: A) => f(a) must_== f(A.negate(a))

    def isSymmetric[A : Integral](a: A) =
      mustBeSymmetric(isEven[A]).apply(a) and
      mustBeSymmetric( isOdd[A]).apply(a)

    def isEvenOrOdd[A : Integral](a: A) =
      isEven(a) must_!= isOdd(a)

    def laws[A : Integral](a: A) =
      isSymmetric(a) and isEvenOrOdd(a)

    "work with Int" in check { laws(_: Int) }
    "work with Long" in check { laws(_: Long) }
    "work with BigInt" in check { laws(_: BigInt) }
  }
}
