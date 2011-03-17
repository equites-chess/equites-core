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

class FieldSpec extends Specification {
  "Field" should {
    "throw an exception on invalid values" in {
      Field(-1,  0) must throwAn[IllegalArgumentException]
      Field( 0, -1) must throwAn[IllegalArgumentException]
      Field(-1, -1) must throwAn[IllegalArgumentException]
      Field( 8,  0) must throwAn[IllegalArgumentException]
      Field( 0,  8) must throwAn[IllegalArgumentException]
      Field( 8,  8) must throwAn[IllegalArgumentException]
    }

/*
    "correctly convert to algebraic notation" in {
      Field(0, 0).toAlgebraicNotation must_== "a1"
      Field(0, 7).toAlgebraicNotation must_== "a8"
      Field(7, 7).toAlgebraicNotation must_== "h8"
      Field(7, 0).toAlgebraicNotation must_== "h1"
    }
*/
/*
    "be constructable from algebraic notation" in {
      Field("a1") must_== Field(0, 0)
      Field("a8") must_== Field(0, 7)
      Field("h8") must_== Field(7, 7)
      Field("h1") must_== Field(7, 0)
    }
*/
/*
    "fail on invalid algebraic notation" in {
      Field("11")  must throwAn[IllegalArgumentException]
      Field("a9")  must throwAn[IllegalArgumentException]
      Field("i1")  must throwAn[IllegalArgumentException]
      Field("a10") must throwAn[IllegalArgumentException]
    }
*/
    "correctly perform +(Vector) and -(Vector)" in {
      Field(1, 1) + Vector( 1,  1) must_== Field(2, 2)
      Field(1, 1) - Vector(-1, -1) must_== Field(2, 2)
      Field(1, 1) - Vector( 1,  1) must_== Field(0, 0)
      Field(1, 1) + Vector(-1, -1) must_== Field(0, 0)

      Field(0, 0) - Vector(1, 1) must throwAn[IllegalArgumentException]
      Field(7, 7) + Vector(1, 1) must throwAn[IllegalArgumentException]
    }
  }
}
