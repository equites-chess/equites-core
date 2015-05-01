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
import eu.timepit.equites.util.MonoidUtil
import eu.timepit.equites.util.PieceAbbr.Wiki._
import eu.timepit.equites.util.SquareAbbr._
import org.specs2.mutable._
import org.specs2.specification.core.Fragments

import scalaz.std.string._
import scalaz.std.vector._

class PgnOpsSpec extends Specification {
  "reconstruct" should {
    def check(pgn: String, actions: Seq[Action]) = {
      val mts = PgnParsers.parseAll(PgnParsers.moveTextSeq, pgn).get
      val init = GameState.init
      val states = GameState.unfold(actions, init).toList
      reconstruct(mts).run(init) must_== states
    }

    val none = Vector.empty[Action]
    val data = Vector(
      ("1. ", none),
      ("e4 ", Vector(Move(pl, e2 to e4))),
      ("e5 ", Vector(Move(pd, e7 to e5))),
      ("2. ", none),
      ("Nf3 ", Vector(Move(nl, g1 to f3))),
      ("Nc6 ", Vector(Move(nd, b8 to c6))),
      ("3. ", none),
      ("Bb5 ", Vector(Move(bl, f1 to b5))),
      ("{This opening is called the Ruy Lopez.}\n", none),
      ("3... ", none),
      ("a6 ", Vector(Move(pd, a7 to a6))),
      ("4. ", none),
      ("Ba4 ", Vector(Move(bl, b5 to a4))),
      ("Nf6 ", Vector(Move(nd, g8 to f6))),
      ("5. ", none),
      ("O-O ", Vector(CastlingShort(White))),
      ("Be7 ", Vector(Move(bd, f8 to e7))),
      ("6. ", none),
      ("Re1 ", Vector(Move(rl, f1 to e1))),
      ("b5 ", Vector(Move(pd, b7 to b5))))

    val monoid = MonoidUtil.product[String, Vector[Action]]
    val accumulated = data.scanLeft(monoid.zero)(monoid.append(_, _))
    val fragments = accumulated.map {
      case (pgn, actions) => s"pass on '$pgn'" in check(pgn, actions)
    }
    Fragments(fragments: _*)
  }
}
