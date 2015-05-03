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
      reconstruct(mts).run(init) == states
    }

    "pass on a complete game" in {
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
        ("b5 ", Vector(Move(pd, b7 to b5))),
        ("7. ", none),
        ("Bb3 ", Vector(Move(bl, a4 to b3))),
        ("d6 ", Vector(Move(pd, d7 to d6))),
        ("8. ", none),
        ("c3 ", Vector(Move(pl, c2 to c3))),
        ("O-O ", Vector(CastlingShort(Black))),
        ("9. ", none),
        ("h3 ", Vector(Move(pl, h2 to h3))),
        ("Nb8\n", Vector(Move(nd, c6 to b8))),
        ("10. ", none),
        ("d4 ", Vector(Move(pl, d2 to d4))),
        ("Nbd7 ", Vector(Move(nd, b8 to d7))),
        ("11. ", none),
        ("c4 ", Vector(Move(pl, c3 to c4))),
        ("c6 ", Vector(Move(pd, c7 to c6))),
        ("12. ", none),
        ("cxb5 ", Vector(Capture(pl, c4 to b5, pd))),
        ("axb5 ", Vector(Capture(pd, a6 to b5, pl))),
        ("13. ", none),
        ("Nc3 ", Vector(Move(nl, b1 to c3))),
        ("Bb7 ", Vector(Move(bd, c8 to b7))),
        ("14. ", none),
        ("Bg5 ", Vector(Move(bl, c1 to g5))),
        ("b4 ", Vector(Move(pd, b5 to b4))),
        ("15. ", none),
        ("Nb1 ", Vector(Move(nl, c3 to b1))),
        ("h6\n", Vector(Move(pd, h7 to h6))),
        ("16. ", none),
        ("Bh4 ", Vector(Move(bl, g5 to h4))),
        ("c5 ", Vector(Move(pd, c6 to c5))),
        ("17. ", none),
        ("dxe5 ", Vector(Capture(pl, d4 to e5, pd))),
        ("Nxe4 ", Vector(Capture(nd, f6 to e4, pl))),
        ("18. ", none),
        ("Bxe7 ", Vector(Capture(bl, h4 to e7, bd))),
        ("Qxe7 ", Vector(Capture(qd, d8 to e7, bl))),
        ("19. ", none),
        ("exd6 ", Vector(Capture(pl, e5 to d6, pd))),
        ("Qf6 ", Vector(Move(qd, e7 to f6))),
        ("20. ", none),
        ("Nbd2 ", Vector(Move(nl, b1 to d2))),
        ("Nxd6\n", Vector(Capture(nd, e4 to d6, pl))),
        ("21. ", none),
        ("Nc4 ", Vector(Move(nl, d2 to c4))),
        ("Nxc4 ", Vector(Capture(nd, d6 to c4, nl))),
        ("22. ", none),
        ("Bxc4 ", Vector(Capture(bl, b3 to c4, nd))),
        ("Nb6 ", Vector(Move(nd, d7 to b6))),
        ("23. ", none),
        ("Ne5 ", Vector(Move(nl, f3 to e5))),
        ("Rae8 ", Vector(Move(rd, a8 to e8))),
        ("24. ", none),
        ("Bxf7+ ", Vector(Capture(bl, c4 to f7, pd))),
        ("Rxf7 ", Vector(Capture(rd, f8 to f7, bl))),
        ("25. ", none),
        ("Nxf7 ", Vector(Capture(nl, e5 to f7, rd))),
        ("Rxe1+\n", Vector(Capture(rd, e8 to e1, rl))),
        ("26. ", none),
        ("Qxe1 ", Vector(Capture(ql, d1 to e1, rd))),
        ("Kxf7 ", Vector(Capture(kd, g8 to f7, nl))),
        ("27. ", none),
        ("Qe3 ", Vector(Move(ql, e1 to e3))),
        ("Qg5 ", Vector(Move(qd, f6 to g5))),
        ("28. ", none),
        ("Qxg5 ", Vector(Capture(ql, e3 to g5, qd))),
        ("hxg5 ", Vector(Capture(pd, h6 to g5, ql))),
        ("29. ", none),
        ("b3 ", Vector(Move(pl, b2 to b3))),
        ("Ke6 ", Vector(Move(kd, f7 to e6))),
        ("30. ", none),
        ("a3 ", Vector(Move(pl, a2 to a3))),
        ("Kd6\n", Vector(Move(kd, e6 to d6))),
        ("31. ", none),
        ("axb4 ", Vector(Capture(pl, a3 to b4, pd))),
        ("cxb4 ", Vector(Capture(pd, c5 to b4, pl))),
        ("32. ", none),
        ("Ra5 ", Vector(Move(rl, a1 to a5))),
        ("Nd5 ", Vector(Move(nd, b6 to d5))),
        ("33. ", none),
        ("f3 ", Vector(Move(pl, f2 to f3))),
        ("Bc8 ", Vector(Move(bd, b7 to c8))),
        ("34. ", none),
        ("Kf2 ", Vector(Move(kl, g1 to f2))),
        ("Bf5 ", Vector(Move(bd, c8 to f5))),
        ("35. ", none),
        ("Ra7 ", Vector(Move(rl, a5 to a7))),
        ("g6\n", Vector(Move(pd, g7 to g6))),
        ("36. ", none),
        ("Ra6+ ", Vector(Move(rl, a7 to a6))),
        ("Kc5 ", Vector(Move(kd, d6 to c5))),
        ("37. ", none),
        ("Ke1 ", Vector(Move(kl, f2 to e1))),
        ("Nf4 ", Vector(Move(nd, d5 to f4))),
        ("38. ", none),
        ("g3 ", Vector(Move(pl, g2 to g3))),
        ("Nxh3 ", Vector(Capture(nd, f4 to h3, pl))),
        ("39. ", none),
        ("Kd2 ", Vector(Move(kl, e1 to d2))),
        ("Kb5 ", Vector(Move(kd, c5 to b5))),
        ("40. ", none),
        ("Rd6 ", Vector(Move(rl, a6 to d6))),
        ("Kc5\n", Vector(Move(kd, b5 to c5))),
        ("41. ", none),
        ("Ra6 ", Vector(Move(rl, d6 to a6))),
        ("Nf2 ", Vector(Move(nd, h3 to f2))),
        ("42. ", none),
        ("g4 ", Vector(Move(pl, g3 to g4))),
        ("Bd3 ", Vector(Move(bd, f5 to d3))),
        ("43. ", none),
        ("Re6", Vector(Move(rl, a6 to e6))))

      val monoid = MonoidUtil.product[String, Vector[Action]]
      val accumulated = data.scanLeft(monoid.zero)(monoid.append(_, _))
      // remove lastOption.toSeq to test each move element individually
      val fragments = accumulated.lastOption.toSeq.map {
        case (pgn, actions) => s"with PGN '$pgn'" in check(pgn, actions)
      }
      Fragments(fragments: _*)
    }

    "pass on a queenside castling" in {
      val pgn = "1. Na3 a6 2. b3 b6 3. Bb2 c6 4. c3 d6 5. Qc2 e6 6. O-O-O"
      val actions = Vector(Move(nl, b1 to a3), Move(pd, a7 to a6), Move(pl, b2 to b3),
        Move(pd, b7 to b6), Move(bl, c1 to b2), Move(pd, c7 to c6), Move(pl, c2 to c3),
        Move(pd, d7 to d6), Move(ql, d1 to c2), Move(pd, e7 to e6), CastlingLong(White))
      check(pgn, actions)
    }

    "pass on an en passant capture" in {
      val pgn = "1. a4 a6 2. a5 b5 3. xb6"
      val actions = Vector(Move(pl, a2 to a4), Move(pd, a7 to a6), Move(pl, a4 to a5),
        Move(pd, b7 to b5), EnPassant(pl, a5 to b6, pd, b5))
      check(pgn, actions)
    }

    // need to test: Promotion, CaptureAndPromotion
  }
}
