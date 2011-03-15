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

class MoveSpec extends Specification {
  "Move" should {
    "be constructable from Field and Vector" in {
      Move(Field(0, 0), Vector( 1,  1)) must_== Move(Field(0, 0), Field(1, 1))
      Move(Field(1, 1), Vector(-1, -1)) must_== Move(Field(1, 1), Field(0, 0))
    }

    "fail on invalid arguments" in {
      Move(Field(1, 1), Vector(-2, -2)) must throwAn[IllegalArgumentException]
      Move(Field(7, 7), Vector( 1,  1)) must throwAn[IllegalArgumentException]
    }

    "correctly convert to algebraic notation" in {
      Move(Field(0, 0), Field(7, 7)).toAlgebraicNotation must_== "a1-h8"
      Move(Field(3, 3), Field(4, 4)).toAlgebraicNotation must_== "d4-e5"
    }

    "be constructable from algebraic notation" in {
      Move("a1-h8") must_== Move(Field(0, 0), Field(7, 7))
      Move("d4-e5") must_== Move(Field(3, 3), Field(4, 4))
    }

    "fail on invalid algebraic notation" in {
      Move("a1-h8a") must throwAn[IllegalArgumentException]
      Move("a1-h")   must throwAn[IllegalArgumentException]
      Move("a1+h8")  must throwAn[IllegalArgumentException]
      Move("a1-h9")  must throwAn[IllegalArgumentException]
    }
  }
}
