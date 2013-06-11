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
package proto

import org.specs2.mutable._

import Uci._
import util.CoordinateMove

class UciSpec extends Specification {
  "Uci.SetOption" should {
    "correctly perform toString with a name and a value" in {
      SetOption("Style", Some("Risky")).toString must_==
        "setoption name Style value Risky"
    }
    "correctly perform toString with a name only" in {
      SetOption("Clear Hash").toString must_== "setoption name Clear Hash"
    }
  }

  "Uci.Id" should {
    "correctly perform toString" in {
      Id("author", "John Doe").toString must_== "id author John Doe"
    }
  }

  "Uci.Bestmove" should {
    "correctly perform toString for a move" in {
      val move = CoordinateMove(Square('e', 2), Square('e', 4))
      Bestmove(move).toString must_== "bestmove e2e4"
    }
    "correctly perform toString for a promotion" in {
      val move =
        CoordinateMove(Square('e', 7), Square('e', 8), Some(Queen(White)))
      Bestmove(move).toString must_== "bestmove e7e8q"
    }
    "correctly perform toString for a move and a ponder" in {
      val move = CoordinateMove(Square('g', 1), Square('f', 3))
      val ponder = Some(CoordinateMove(Square('d', 8), Square('f', 6)))
      Bestmove(move, ponder).toString must_== "bestmove g1f3 ponder d8f6"
    }
  }
}
