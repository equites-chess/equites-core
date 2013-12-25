// Equites, a Scala chess playground
// Copyright © 2011-2013 Frank S. Thomas <frank@timepit.eu>
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
  "Square companion" should {
    "correctly perform validCoordinates" in {
      validCoordinates( 4,  4) must beTrue
      validCoordinates( 0,  0) must beTrue
      validCoordinates( 7,  7) must beTrue
      validCoordinates(-1,  4) must beFalse
      validCoordinates( 1,  8) must beFalse
      validCoordinates( 1, -2) must beFalse
    }

    "correctly calculate l1Dist and lInfDist" in {
      val s1 = Square(1, 2)
      val s2 = Square(4, 3)

      l1Dist(s1, s2)   must_== 4
      lInfDist(s1, s2) must_== 3

      val s3 = Square(0, 0)
      val s4 = Square(1, 1)

      l1Dist(s3, s4)   must_== 2
      lInfDist(s3, s4) must_== 1
    }
  }

  "Square" should {
    "satisfy the Equal laws" in check(equal.laws[Square])
    "satisfy the Order laws" in check(order.laws[Square])

    "correctly perform +(Vec) and -(Vec)" in {
      Square(1, 1) + Vec( 1,  1) must_== Square(2, 2)
      Square(1, 1) - Vec(-1, -1) must_== Square(2, 2)
      Square(1, 1) - Vec( 1,  1) must_== Square(0, 0)
      Square(1, 1) + Vec(-1, -1) must_== Square(0, 0)

      (Square(0, 0) - Vec(1, 1)).isValid must beFalse
      (Square(7, 7) + Vec(1, 1)).isValid must beFalse
    }

    "correctly perform +(Square)" in {
      Square(1, 1) + Square(1, 1) must_== Vec(2, 2)
      Square(2, 2) + Square(1, 1) must_== Vec(3, 3)
      Square(2, 1) + Square(0, 0) must_== Vec(2, 1)
      Square(0, 0) + Square(3, 4) must_== Vec(3, 4)
    }

    "correctly perform -(Square)" in {
      Square(1, 1) - Square(1, 1) must_== Vec(0, 0)
      Square(2, 2) - Square(1, 1) must_== Vec(1, 1)
      Square(2, 1) - Square(0, 0) must_== Vec(2, 1)
      Square(0, 0) - Square(3, 4) must_== Vec(-3, -4)
    }

    "correctly perform isValid" in {
      Square(0,  0).isValid must beTrue
      Square(0,  7).isValid must beTrue
      Square(7,  0).isValid must beTrue
      Square(7,  7).isValid must beTrue

      Square(-1,  0).isValid must beFalse
      Square( 0, -1).isValid must beFalse
      Square(-1, -1).isValid must beFalse
      Square( 8,  0).isValid must beFalse
      Square( 0,  8).isValid must beFalse
      Square( 8,  8).isValid must beFalse
    }

    "correctly perform isLight and isDark" in {
      Square(0, 0).isDark must beTrue
      Square(7, 7).isDark must beTrue
      Square(0, 2).isDark must beTrue
      Square(2, 0).isDark must beTrue

      Square(0, 1).isLight must beTrue
      Square(0, 3).isLight must beTrue
      Square(1, 0).isLight must beTrue
      Square(3, 0).isLight must beTrue
    }

    "correctly calculate the distance to the board boundary" in check {
      (sq: Square) => sq.distToBoundary must beBetween(0, 3)
    }

    "up.right.down.left must be the identity" in check {
      (sq: Square) => sq.up.right.down.left must_== sq
    }

    "be constructible with algebraic arguments" in {
      Square(0, 0) must_== Square('a', 1)
      Square(7, 7) must_== Square('h', 8)

      Square('i', 1).isValid must beFalse
      Square('a', 9).isValid must beFalse
    }
  }
}
