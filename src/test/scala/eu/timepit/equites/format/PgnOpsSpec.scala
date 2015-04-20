// Equites, a Scala chess playground
// Copyright © 2014-2015 Frank S. Thomas <frank@timepit.eu>
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
package format

import eu.timepit.equites.format.PgnOps._
import eu.timepit.equites.util.PieceAbbr.Wiki._
import eu.timepit.equites.util.SquareAbbr._
import org.specs2.mutable._

class PgnOpsSpec extends Specification {
  def c(pgn: String, actions: Seq[Action]) = {
    val mts = PgnParsers.parseAll(PgnParsers.moveTextSeq, pgn).get
    val init = GameState.init
    val states = GameState.unfold(actions, init).toList
    construct(mts).run(init) must_== states
  }

  "x" should {
    "0" in {
      c("", Seq.empty)
    }

    "2" in c("1.", Seq.empty)

    "1" in {
      c("e4", Seq(Move(pl, e2 to e4)))
    }
  }
}
