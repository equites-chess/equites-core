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
import Square._

class SquareSpec extends Specification {
  "object Square" should {
    "correctly perform validCoordinates" in {
      validCoordinates( 4,  4) must_== true
      validCoordinates( 0,  0) must_== true
      validCoordinates( 7,  7) must_== true
      validCoordinates(-1,  4) must_== false
      validCoordinates( 1,  8) must_== false
      validCoordinates( 1, -2) must_== false
    }

    "correctly perform validSum" in {
      validSum(Square(0, 0), Vector( 1,  1)) must_== true
      validSum(Square(1, 1), Vector(-1, -1)) must_== true
      validSum(Square(0, 0), Vector(-1, -1)) must_== false
      validSum(Square(7, 7), Vector( 1,  1)) must_== false
      validSum(Square(7, 7), Vector( 1,  1)) must_== false
    }

    "correctly calculate l1Dist and lInfDist" in {
      var p1 = Square(1, 2)
      var p2 = Square(4, 3)

      l1Dist(p1, p2)   must_== 4
      lInfDist(p1, p2) must_== 3

      p1 = Square(0, 0)
      p2 = Square(1, 1)

      l1Dist(p1, p2)   must_== 2
      lInfDist(p1, p2) must_== 1
    }
  }

  "class Square" should {
    "throw an exception on invalid values" in {
      Square(-1,  0) must throwAn[IllegalArgumentException]
      Square( 0, -1) must throwAn[IllegalArgumentException]
      Square(-1, -1) must throwAn[IllegalArgumentException]
      Square( 8,  0) must throwAn[IllegalArgumentException]
      Square( 0,  8) must throwAn[IllegalArgumentException]
      Square( 8,  8) must throwAn[IllegalArgumentException]
    }

    "correctly perform +(Vector) and -(Vector)" in {
      Square(1, 1) + Vector( 1,  1) must_== Square(2, 2)
      Square(1, 1) - Vector(-1, -1) must_== Square(2, 2)
      Square(1, 1) - Vector( 1,  1) must_== Square(0, 0)
      Square(1, 1) + Vector(-1, -1) must_== Square(0, 0)

      Square(0, 0) - Vector(1, 1) must throwAn[IllegalArgumentException]
      Square(7, 7) + Vector(1, 1) must throwAn[IllegalArgumentException]
    }

    "correctly perform -(Square)" in {
      Square(1, 1) - Square(1, 1) must_== Vector(0, 0)
      Square(2, 2) - Square(1, 1) must_== Vector(1, 1)
      Square(2, 1) - Square(0, 0) must_== Vector(2, 1)
      Square(0, 0) - Square(3, 4) must_== Vector(-3, -4)
    }

    "correctly perform isLight and isDark" in {
      Square(0, 0).isDark must_== true
      Square(7, 7).isDark must_== true
      Square(0, 2).isDark must_== true
      Square(2, 0).isDark must_== true

      Square(0, 1).isLight must_== true
      Square(0, 3).isLight must_== true
      Square(1, 0).isLight must_== true
      Square(3, 0).isLight must_== true
    }
  }
}
