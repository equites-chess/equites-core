// Equites, a simple chess interface
// Copyright © 2011, 2013 Frank S. Thomas <f.thomas@gmx.de>
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
package implicits

import org.specs2.mutable._

import RichSquareImplicit._

class RichSquareSpec extends Specification {
  "RichSquare" should {
    "correctly perform toAlgebraic" in {
      Square(0, 0).toAlgebraic must_== "a1"
      Square(1, 1).toAlgebraic must_== "b2"
      Square(0, 7).toAlgebraic must_== "a8"
      Square(7, 0).toAlgebraic must_== "h1"
      Square(7, 7).toAlgebraic must_== "h8"
      Square(3, 3).toAlgebraic must_== "d4"
    }

    "correctly perform toNumeric" in {
      Square(0, 0).toNumeric must_== "11"
      Square(1, 1).toNumeric must_== "22"
      Square(0, 7).toNumeric must_== "18"
      Square(7, 0).toNumeric must_== "81"
      Square(7, 7).toNumeric must_== "88"
      Square(3, 3).toNumeric must_== "44"
    }
  }
}
