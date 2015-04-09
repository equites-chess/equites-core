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

import org.scalacheck.Arbitrary
import org.specs2.ScalaCheck
import org.specs2.matcher.DataTables
import org.specs2._
import scala.reflect._

import Math._

class MathSpec extends Specification with DataTables with ScalaCheck {
  def is =
    "gcd should" ^
      "be symmetric in its arguments" ! prop {
        (a: Int, b: Int) => (a >= 0 && b >= 0) ==> (gcd(a, b) must_== gcd(b, a))
      } ^
      p ^
      "isEven and isOdd should" ^
      "yield correct results for some positive numbers" ! {
        "a" | "isEven" |
          0 ! true |
          1 ! false |
          2 ! true |
          3 ! false |
          4 ! true |> {
            (a, r) => (isEven(a) must_== r) and (isOdd(a) must_== !r)
          }
      } ^
      br ^
      workWith[Byte] ^
      workWith[Short] ^
      workWith[Int] ^
      workWith[Long] ^
      workWith[BigInt]

  def workWith[A: Arbitrary: Integral: ClassTag] =
    s"work with ${classTag[A]}" ^
      eitherEvenOrOdd[A] ^
      beEvenFunctions[A] ^
      p

  def eitherEvenOrOdd[A: Arbitrary: Integral] =
    "yield different results for the same input" ! prop {
      (a: A) => isEven(a) must_!= isOdd(a)
    }

  def beEvenFunctions[A: Arbitrary: Integral] =
    "be even functions" ! prop {
      (a: A) =>
        beEvenFunction(isEven[A]).apply(a) and
          beEvenFunction(isOdd[A]).apply(a)
    }

  def beEvenFunction[A, B](f: A => B)(implicit A: Numeric[A]) =
    (a: A) => f(a) must_== f(A.negate(a))
}
