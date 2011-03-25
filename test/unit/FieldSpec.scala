// Equites, a simple chess interface
// Copyright © 2011 Frank S. Thomas <f.thomas@gmx.de>
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

package equites

import org.specs2.mutable._
import Field._

class FieldSpec extends Specification {
  "object Field" should {
    "correctly perform validCoordinates" in {
      validCoordinates( 4,  4) must_== true
      validCoordinates( 0,  0) must_== true
      validCoordinates( 7,  7) must_== true
      validCoordinates(-1,  4) must_== false
      validCoordinates( 1,  8) must_== false
      validCoordinates( 1, -2) must_== false
    }

    "correctly perform validSum" in {
      validSum(Field(0, 0), Vector( 1,  1)) must_== true
      validSum(Field(1, 1), Vector(-1, -1)) must_== true
      validSum(Field(0, 0), Vector(-1, -1)) must_== false
      validSum(Field(7, 7), Vector( 1,  1)) must_== false
      validSum(Field(7, 7), Vector( 1,  1)) must_== false
    }

    "correctly calculate l1Dist and lInfDist" in {
      var p1 = Field(1, 2)
      var p2 = Field(4, 3)

      l1Dist(p1, p2)   must_== 4
      lInfDist(p1, p2) must_== 3

      p1 = Field(0, 0)
      p2 = Field(1, 1)

      l1Dist(p1, p2)   must_== 2
      lInfDist(p1, p2) must_== 1
    }
  }

  "class Field" should {
    "throw an exception on invalid values" in {
      Field(-1,  0) must throwAn[IllegalArgumentException]
      Field( 0, -1) must throwAn[IllegalArgumentException]
      Field(-1, -1) must throwAn[IllegalArgumentException]
      Field( 8,  0) must throwAn[IllegalArgumentException]
      Field( 0,  8) must throwAn[IllegalArgumentException]
      Field( 8,  8) must throwAn[IllegalArgumentException]
    }

    "correctly perform +(Vector) and -(Vector)" in {
      Field(1, 1) + Vector( 1,  1) must_== Field(2, 2)
      Field(1, 1) - Vector(-1, -1) must_== Field(2, 2)
      Field(1, 1) - Vector( 1,  1) must_== Field(0, 0)
      Field(1, 1) + Vector(-1, -1) must_== Field(0, 0)

      Field(0, 0) - Vector(1, 1) must throwAn[IllegalArgumentException]
      Field(7, 7) + Vector(1, 1) must throwAn[IllegalArgumentException]
    }

    "correctly perform -(Field)" in {
      Field(1, 1) - Field(1, 1) must_== Vector(0, 0)
      Field(2, 2) - Field(1, 1) must_== Vector(1, 1)
      Field(2, 1) - Field(0, 0) must_== Vector(2, 1)
      Field(0, 0) - Field(3, 4) must_== Vector(-3, -4)
    }
  }
}
