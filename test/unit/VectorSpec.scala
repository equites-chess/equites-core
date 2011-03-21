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

class VectorSpec extends Specification {
  "Vector" should {
    "correctly perform +(Vector) and -(Vector)" in {
      Vector(1, 1) + Vector( 2,  2) must_== Vector( 3,  3)
      Vector(1, 1) + Vector(-2, -2) must_== Vector(-1, -1)

      Vector(1, 1) - Vector( 2,  2) must_== Vector(-1, -1)
      Vector(1, 1) - Vector(-2, -2) must_== Vector( 3,  3) 
    }

    "correctly perform *(Int) and /(Int)" in {
      Vector(1, 2) * -1 must_== Vector(-1, -2)
      Vector(1, 2) *  0 must_== Vector( 0,  0)
      Vector(1, 2) *  2 must_== Vector( 2,  4)

      Vector(1, 2) / -1 must_== Vector(-1, -2)
      Vector(1, 2) /  2 must_== Vector( 0,  1)
      Vector(1, 2) /  3 must_== Vector( 0,  0)
      Vector(4, 6) /  2 must_== Vector( 2,  3)
    }

    "correctly perform map" in {
      Vector( 0,  0).map(_ + 1) must_== Vector(1, 1)
      Vector(-1, -1).map(_.abs) must_== Vector(1, 1)
    }

    "correctly perform max and sum" in {
      Vector(1,  2).max must_== 2
      Vector(1, -2).max must_== 1

      Vector(1,  2).sum must_==  3
      Vector(1, -2).sum must_== -1
    }
  }
}
