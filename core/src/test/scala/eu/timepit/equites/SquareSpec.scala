// Equites, a Scala chess playground
// Copyright © 2011-2014 Frank S. Thomas <frank@timepit.eu>
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

import org.specs2.ScalaCheck
import org.specs2.mutable._
import scalaz.scalacheck.ScalazProperties._

import Square._

import ArbitraryInstances._

class SquareSpec extends Specification with ScalaCheck {
  "Square" should {
    "satisfy the Equal laws" in check(equal.laws[Square])
    "satisfy the Order laws" in check(order.laws[Square])

    "correctly perform +(Vec) and -(Vec)" in {
      Square.unsafeFrom(1, 1) + Vec(1, 1) must_== Some(Square.unsafeFrom(2, 2))
      Square.unsafeFrom(1, 1) - Vec(-1, -1) must_== Some(Square.unsafeFrom(2, 2))
      Square.unsafeFrom(1, 1) - Vec(1, 1) must_== Some(Square.unsafeFrom(0, 0))
      Square.unsafeFrom(1, 1) + Vec(-1, -1) must_== Some(Square.unsafeFrom(0, 0))
    }

    "correctly perform +(Square)" in {
      Square.unsafeFrom(1, 1) + Square.unsafeFrom(1, 1) must_== Vec(2, 2)
      Square.unsafeFrom(2, 2) + Square.unsafeFrom(1, 1) must_== Vec(3, 3)
      Square.unsafeFrom(2, 1) + Square.unsafeFrom(0, 0) must_== Vec(2, 1)
      Square.unsafeFrom(0, 0) + Square.unsafeFrom(3, 4) must_== Vec(3, 4)
    }

    "correctly perform -(Square)" in {
      Square.unsafeFrom(1, 1) - Square.unsafeFrom(1, 1) must_== Vec(0, 0)
      Square.unsafeFrom(2, 2) - Square.unsafeFrom(1, 1) must_== Vec(1, 1)
      Square.unsafeFrom(2, 1) - Square.unsafeFrom(0, 0) must_== Vec(2, 1)
      Square.unsafeFrom(0, 0) - Square.unsafeFrom(3, 4) must_== Vec(-3, -4)
    }

    "correctly perform isLight and isDark" in {
      Square.unsafeFrom(0, 0).isDark must beTrue
      Square.unsafeFrom(7, 7).isDark must beTrue
      Square.unsafeFrom(0, 2).isDark must beTrue
      Square.unsafeFrom(2, 0).isDark must beTrue

      Square.unsafeFrom(0, 1).isLight must beTrue
      Square.unsafeFrom(0, 3).isLight must beTrue
      Square.unsafeFrom(1, 0).isLight must beTrue
      Square.unsafeFrom(3, 0).isLight must beTrue
    }

    "correctly calculate the distance to the board boundary" in check {
      (sq: Square) => sq.distToBoundary must beBetween(0, 3)
    }
  }
}
