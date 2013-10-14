// Equites, a Scala chess playground
// Copyright © 2013 Frank S. Thomas <frank@timepit.eu>
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
package util

import org.specs2.mutable._

import util.PieceAbbr.Wiki._

class CoordinateMoveSpec extends Specification {
  "CoordinateMove" should {
    "apply promotions" in {
      val promotion = Promotion(pl, Square(0, 6), Square(0, 7), rl)
      CoordinateMove(promotion) must_==
        CoordinateMove(Square(0, 6), Square(0, 7), Some(rl))
    }
    "apply moves" in {
      val move = Move(ql, Square(0, 0), Square(3, 3))
      CoordinateMove(move) must_== CoordinateMove(Square(0, 0), Square(3, 3))
    }
    "apply castlings" in {
      val castling = CastlingLong(White)
      CoordinateMove(castling) must_== CoordinateMove(castling.kingMove)
    }
  }
}
