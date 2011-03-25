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
import Directions._

class DirectionsSpec extends Specification {
  "class Directions" should {
    "correctly perform inverse" in {
      Directions.front.inverse must_== Directions.back
      Directions.right.inverse must_== Directions.left
      diagonalFront.inverse must_== diagonalBack
      knightLike.inverse    must_== knightLike
    }

    "correctly perform inverseIfBlack" in {
      front.inverseIfBlack(White) must_== front
      front.inverseIfBlack(Black) must_== back
    }
  }
}
