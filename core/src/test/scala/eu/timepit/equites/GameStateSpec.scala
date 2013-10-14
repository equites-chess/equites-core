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

import implicits.ActionImplicits._
import implicits.GameStateImplicits._
import util.PieceAbbr.Wiki._

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

    val states = GameState.unfold(actions)

    def moveIndicator(i: Int): String =
      states(i).moveNumberIndicator + " " + actions(i).toLongFigurine

    "record the starting position" in {
      states(0).board must_== Rules.startingBoard
      states(0).lastAction must beNone
      states(0).color must_== White
      states(0).moveNumber must_== 1
      states(0).halfmoveClock must_== 0
      states(0).availableCastlings must_== Rules.allCastlings.toSet
      states(0).toFen must_==
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    }

    "record " + moveIndicator(0) in {
      states(1).board must_== states(0).board.processAction(actions(0))
      states(1).lastAction must beSome(actions(0))
      states(1).color must_== Black
      states(1).moveNumber must_== 1
      states(1).halfmoveClock must_== 0
      states(1).availableCastlings must_== Rules.allCastlings.toSet
      states(1).toFen must_==
        "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"
    }

    "record " + moveIndicator(1) in {
      states(2).board must_== states(1).board.processAction(actions(1))
      states(2).lastAction must beSome(actions(1))
      states(2).color must_== White
      states(2).moveNumber must_== 2
      states(2).halfmoveClock must_== 0
      states(2).availableCastlings must_== Rules.allCastlings.toSet
      states(2).toFen must_==
        "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2"
    }

    "record " + moveIndicator(2) in {
      states(3).board must_== states(2).board.processAction(actions(2))
      states(3).lastAction must beSome(actions(2))
      states(3).color must_== Black
      states(3).moveNumber must_== 2
      states(3).halfmoveClock must_== 1
      states(3).availableCastlings must_== Rules.allCastlings.toSet
      states(3).toFen must_==
        "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2"
    }

    "record " + moveIndicator(5) in {
      states(6).board must_== states(5).board.processAction(actions(5))
      states(6).lastAction must beSome(actions(5))
      states(6).color must_== White
      states(6).moveNumber must_== 4
      states(6).halfmoveClock must_== 0
      states(6).availableCastlings must_== Rules.allCastlings.toSet
      states(6).toFen must_==
        "r1bqkbnr/pp2pppp/n2p4/2p5/4P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 4"
    }

    "record " + moveIndicator(6) in {
      states(7).board must_== states(6).board.processAction(actions(6))
      states(7).lastAction must beSome(actions(6))
      states(7).color must_== Black
      states(7).moveNumber must_== 4
      states(7).halfmoveClock must_== 1
      states(7).availableCastlings must_== Rules.castlingsBy(Black).toSet
      states(7).toFen must_==
        "r1bqkbnr/pp2pppp/n2p4/2p5/4P3/5N2/PPPP1PPP/RNBQ1RK1 b kq - 1 4"
    }

    "handle valid coordinate moves" in {
      val move = Move(pl, Square('e', 2), Square('e', 4))
      val cm = util.CoordinateMove(move)
      GameState.init.updated(cm) must beSome(GameState.init.updated(move))
    }
    "handle invalid coordinate moves" in {
      val cm = util.CoordinateMove(Square('a', 3), Square('a', 4))
      GameState.init.updated(cm) must beNone
    }
  }
}
