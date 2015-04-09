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

import util.SquareAbbr._
import Square._

import ArbitraryInstances._

class SquareSpec extends Specification with ScalaCheck {
  "Square" should {
    "satisfy the Equal laws" in equal.laws[Square]
    "satisfy the Order laws" in order.laws[Square]

    "correctly perform +(Vec) and -(Vec)" in {
      b2 + Vec(+1, +1) must_== Some(c3)
      b2 - Vec(-1, -1) must_== Some(c3)
      b2 - Vec(+1, +1) must_== Some(a1)
      b2 + Vec(-1, -1) must_== Some(a1)
    }

    "correctly perform +(Square)" in {
      b2 + b2 must_== Vec(2, 2)
      c3 + b2 must_== Vec(3, 3)
      c2 + a1 must_== Vec(2, 1)
      a1 + d5 must_== Vec(3, 4)
    }

    "correctly perform -(Square)" in {
      b2 - b2 must_== Vec(+0, +0)
      c3 - b2 must_== Vec(+1, +1)
      c2 - a1 must_== Vec(+2, +1)
      a1 - d5 must_== Vec(-3, -4)
    }

    "correctly perform isLight and isDark" in {
      a1.isDark must beTrue
      h8.isDark must beTrue
      a3.isDark must beTrue
      c1.isDark must beTrue

      a2.isLight must beTrue
      a4.isLight must beTrue
      b1.isLight must beTrue
      d1.isLight must beTrue
    }

    "correctly calculate the distance to the board boundary" in prop {
      (sq: Square) => sq.minDistToBounds must beBetween(0, 3)
    }
  }
}
