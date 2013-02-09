// Equites, a Scala chess playground
// Copyright © 2011 Frank S. Thomas <frank@timepit.eu>
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

import Rules._

class RulesSpec extends Specification {
  "Rules" should {
    "correctly perform squaresInDirection" in {
      squaresInDirection(Square(3, 3), Vec(1, 1)).toList must_==
        List(Square(4, 4), Square(5, 5), Square(6, 6), Square(7, 7))
      squaresInDirection(Square(3, 3), Vec(-1, 0)).toList must_==
        List(Square(2, 3), Square(1, 3), Square(0, 3))
      squaresInDirection(Square(3, 3), Vec(3, 1)).toList must_==
        List(Square(6, 4))
    }

    "correctly perform possibleSquares" in {
      possibleSquares(Placed(King(White), Square(3, 2))).toSet must_==
        Set(Square(3, 3), Square(4, 3), Square(4, 2), Square(4, 1),
            Square(3, 1), Square(2, 1), Square(2, 2), Square(2, 3))

      possibleSquares(Placed(Pawn(White), Square(3, 3))).toSet must_==
        Set(Square(3, 4))
      possibleSquares(Placed(Pawn(White), Square(0, 1))).toSet must_==
        Set(Square(0, 2), Square(0, 3))
      possibleSquares(Placed(Pawn(Black), Square(0, 6))).toSet must_==
        Set(Square(0, 5), Square(0, 4))
    }

    "correctly perform possibleSquares for Bishop" in {
      possibleSquares(Placed(Bishop(White), Square(3, 3))).toSet must_==
        Set(Square(0, 0), Square(1, 1), Square(2, 2), Square(4, 4),
            Square(5, 5), Square(6, 6), Square(7, 7),
            Square(0, 6), Square(1, 5), Square(2, 4), Square(4, 2),
            Square(5, 1), Square(6, 0))
    }
  }
}
