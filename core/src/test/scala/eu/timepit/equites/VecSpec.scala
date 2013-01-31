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

import org.specs2.mutable._

class VecSpec extends Specification {
  "Vec" should {
    "correctly perform map" in {
      Vec( 0,  0).map(_ + 1) must_== Vec(1, 1)
      Vec(-1, -1).map(_.abs) must_== Vec(1, 1)
    }

    "correctly perform +(Vec) and -(Vec)" in {
      Vec(1, 1) + Vec( 2,  2) must_== Vec( 3,  3)
      Vec(1, 1) + Vec(-2, -2) must_== Vec(-1, -1)

      Vec(1, 1) - Vec( 2,  2) must_== Vec(-1, -1)
      Vec(1, 1) - Vec(-2, -2) must_== Vec( 3,  3) 
    }

    "correctly perform unary_-" in {
      -Vec( 1,  3) must_== Vec(-1, -3)
      -Vec(-3, -1) must_== Vec( 3,  1)
    }

    "correctly perform *(Int) and /(Int)" in {
      Vec(1, 2) * -1 must_== Vec(-1, -2)
      Vec(1, 2) *  0 must_== Vec( 0,  0)
      Vec(1, 2) *  2 must_== Vec( 2,  4)

      Vec(1, 2) / -1 must_== Vec(-1, -2)
      Vec(1, 2) /  2 must_== Vec( 0,  1)
      Vec(1, 2) /  3 must_== Vec( 0,  0)
      Vec(4, 6) /  2 must_== Vec( 2,  3)
    }

    "correctly perform max, min, and sum" in {
      Vec(1,  2).max must_== 2
      Vec(1, -2).max must_== 1

      Vec(1,  2).min must_==  1
      Vec(1, -2).min must_== -2

      Vec(1,  2).sum must_==  3
      Vec(1, -2).sum must_== -1

      val v1 = Vec(1, -2)
      v1.file + v1.rank must_== v1.sum
      v1.max + v1.min must_== v1.sum
    }
  }
}
