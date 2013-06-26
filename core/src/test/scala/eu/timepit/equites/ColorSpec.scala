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

class ColorSpec extends Specification with ScalaCheck {
  "Color" should {
    "satisfy the Equal laws" in check(equal.laws[Color])
    "satisfy the Order laws" in check(order.laws[Color])
  }

  "White should return Black as opposite Color" in {
      White.opposite must_== Black
  }
  "Black should return White as opposite Color" in {
    Black.opposite must_== White
  }
}
