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

import org.specs2.mutable._

import implicits.SquareImplicits._
import util.PieceAbbr._

class GameStateSpec extends Specification {
  "GameState" should {
    val state0 = GameState.init

    val move1 = Move(pl, Square('e', 2), Square('e', 4))
    val state1 = state0.applyAction(move1)

    val move2 = Move(pd, Square('c', 7), Square('c', 5))
    val state2 = state1.applyAction(move2)

    val move3 = Move(nl, Square('g', 1), Square('f', 3))
    val state3 = state2.applyAction(move3)

    "correctly record move1" in {
      state1.board must_== state0.board.processAction(move1)
      state1.lastAction must beSome(move1)
      state1.color must_== Black
      state1.moveNumber must_== 1
      state1.halfmoveClock must_== 0
    }

    "correctly record move2" in {
      state2.board must_== state1.board.processAction(move2)
      state2.lastAction must beSome(move2)
      state2.color must_== White
      state2.moveNumber must_== 2
      state2.halfmoveClock must_== 0
    }

    "correctly record move3" in {
      state3.board must_== state2.board.processAction(move3)
      state3.lastAction must beSome(move3)
      state3.color must_== Black
      state3.moveNumber must_== 2
      state3.halfmoveClock must_== 1
    }
  }
}
