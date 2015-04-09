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
import util.SquareAbbr._

class CoordinateActionSpec extends Specification {
  "CoordinateAction" should {
    "apply promotions" in {
      val promotion = Promotion(pl, a7 to a8, rl)
      CoordinateAction(promotion) must_== CoordinateAction(a7 to a8, Some(rl))
    }
    "apply moves" in {
      val move = Move(ql, a1 to d4)
      CoordinateAction(move) must_== CoordinateAction(a1 to d4)
    }
    "apply castlings" in {
      val castling = CastlingLong(White)
      CoordinateAction(castling) must_== CoordinateAction(castling.kingMove)
    }
  }
}
