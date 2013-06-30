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
import scalaz.std.stream

import implicits.ActionImplicits._
import util.PieceAbbr._

class GameStateSpec extends Specification {
  "GameState" should {
    val actions = Vector(
        Move(pl, Square('e', 2), Square('e', 4)),
        Move(pd, Square('c', 7), Square('c', 5)),
        Move(nl, Square('g', 1), Square('f', 3)),
        Move(pd, Square('d', 7), Square('d', 6)),
        Move(bl, Square('f', 1), Square('a', 6)),
        Capture(nd, Square('b', 8), Square('a', 6), bl),
        CastlingShort(White))

    val states = GameState.init #:: stream.unfold((GameState.init, actions)) {
      case (state, actions) => actions.headOption.map { action =>
        val updated = state.updated(action)
        (updated, (updated, actions.tail))
      }
    }

    "record " + actions(0).toLongFigurine in {
      states(1).board must_== states(0).board.processAction(actions(0))
      states(1).lastAction must beSome(actions(0))
      states(1).color must_== Black
      states(1).moveNumber must_== 1
      states(1).halfmoveClock must_== 0
      states(1).availableCastlings must_== Rules.allCastlings.toSet
    }

    "record " + actions(1).toLongFigurine in {
      states(2).board must_== states(1).board.processAction(actions(1))
      states(2).lastAction must beSome(actions(1))
      states(2).color must_== White
      states(2).moveNumber must_== 2
      states(2).halfmoveClock must_== 0
      states(2).availableCastlings must_== Rules.allCastlings.toSet
    }

    "record " + actions(2).toLongFigurine in {
      states(3).board must_== states(2).board.processAction(actions(2))
      states(3).lastAction must beSome(actions(2))
      states(3).color must_== Black
      states(3).moveNumber must_== 2
      states(3).halfmoveClock must_== 1
      states(3).availableCastlings must_== Rules.allCastlings.toSet
    }

    "record " + actions(5).toLongFigurine in {
      states(6).board must_== states(5).board.processAction(actions(5))
      states(6).lastAction must beSome(actions(5))
      states(6).color must_== White
      states(6).moveNumber must_== 4
      states(6).halfmoveClock must_== 0
      states(6).availableCastlings must_== Rules.allCastlings.toSet
    }

    "record " + actions(6).toLongFigurine in {
      states(7).board must_== states(6).board.processAction(actions(6))
      states(7).lastAction must beSome(actions(6))
      states(7).color must_== Black
      states(7).moveNumber must_== 4
      states(7).halfmoveClock must_== 1
      states(7).availableCastlings must_==
        Set(CastlingLong(White), CastlingShort(Black), CastlingLong(Black))
    }
  }
}
