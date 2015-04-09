// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
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
import scala.concurrent.duration._

import proto.Uci._
import util.GameStateUtil
import util.CoordinateAction
import util.PieceAbbr.Wiki._
import util.SquareAbbr._

class UciSpec extends Specification {
  "Uci.Uci" >> {
    "toString should return 'uci'" in {
      Uci.Uci.toString must_== "uci"
    }
  }

  "Uci.Debug" >> {
    "toString should return 'debug on' if on is true" in {
      Debug(on = true).toString must_== "debug on"
    }
    "toString should return 'debug off' if on is false" in {
      Debug(on = false).toString must_== "debug off"
    }
  }

  "Uci.IsReady" >> {
    "toString should return 'isready'" in {
      IsReady.toString must_== "isready"
    }
  }

  "Uci.SetOption" >> {
    "toString should exclude value if it is empty" in {
      SetOption("Clear Hash").toString must_==
        "setoption name Clear Hash"
    }
    "toString should include value if it is not empty" in {
      SetOption("Style", Some("Risky")).toString must_==
        "setoption name Style value Risky"
    }
  }

  "Uci.UciNewGame" >> {
    "toString should return 'ucinewgame'" in {
      UciNewGame.toString must_== "ucinewgame"
    }
  }

  "Uci.Position" >> {
    val fen = GameStateUtil.showFen(GameState.init)

    "toString should return 'position startpos moves' if history is empty" in {
      Position(Seq.empty).toString must_== "position startpos moves"
    }
    "toString should return no moves if history contains only the initial state" in {
      Position(Seq(GameState.init)).toString must_==
        s"position fen $fen moves"
    }
    "toString should return one move if history contains one move" in {
      val actions = Seq(Move(pl, e2 to e4))
      val history = GameState.unfold(actions)

      Position(history).toString must_==
        s"position fen $fen moves e2e4"
    }
    "toString should return two moves if history contains two moves" in {
      val actions = Seq(Move(pl, e2 to e4),
        Move(nd, Draw(g8, f6)))
      val history = GameState.unfold(actions)

      Position(history).toString must_==
        s"position fen $fen moves e2e4 g8f6"
    }
  }

  "Uci.Go" >> {
    import Go._

    "toString should return 'go ponder'" in {
      Go(Ponder).toString must_== "go ponder"
    }
    "toString should return 'go depth ...'" in {
      Go(Depth(5)).toString must_== "go depth 5"
    }
    "toString should return 'go movetime ...'" in {
      Go(Movetime(0.1.second)).toString must_== "go movetime 100"
    }
    "toString should return 'go infinite'" in {
      Go(Infinite).toString must_== "go infinite"
    }
    "toString should return the expected result on multiple arguments" in {
      Go(Ponder, Infinite).toString must_== "go ponder infinite"
    }
  }

  "Uci.Stop" >> {
    "toString should return 'stop'" in {
      Stop.toString must_== "stop"
    }
  }

  "Uci.Id" >> {
    "toString should return the expected result" in {
      Id("author", "John Doe").toString must_== "id author John Doe"
    }
  }

  "Uci.Bestmove" >> {
    "toString should return the expected result on a move" in {
      val move = CoordinateAction(e2 to e4)
      Bestmove(move).toString must_== "bestmove e2e4"
    }
    "toString should return the expected result on a promotion" in {
      val move = CoordinateAction(e7 to e8, Some(ql))
      Bestmove(move).toString must_== "bestmove e7e8q"
    }
    "toString should return the expected result on a move and a ponder" in {
      val move = CoordinateAction(g1 to f3)
      val ponder = Some(CoordinateAction(d8 to f6))
      Bestmove(move, ponder).toString must_== "bestmove g1f3 ponder d8f6"
    }
  }

  "Uci.UciOption" >> {
    import UciOption._

    "toString should return 'option ... type check ...'" in {
      UciOption("Ponder", Check(default = true)).toString must_==
        "option name Ponder type check default true"
    }
    "toString should return 'option ... type spin ...'" in {
      UciOption("Hash", Spin(32, 4, 4096)).toString must_==
        "option name Hash type spin default 32 min 4 max 4096"
    }
    "toString should return 'option ... type combo ...'" in {
      UciOption("Style", Combo("Normal", Seq("Solid", "Normal", "Risky"))).toString must_==
        "option name Style type combo default Normal var Solid var Normal var Risky"
    }
    "toString should return 'option ... type button'" in {
      UciOption("Clear Hash", Button).toString must_==
        "option name Clear Hash type button"
    }
    "toString should return 'option ... type string ...'" in {
      UciOption("Book File", StringType("book.bin")).toString must_==
        "option name Book File type string default book.bin"
    }
  }
}
