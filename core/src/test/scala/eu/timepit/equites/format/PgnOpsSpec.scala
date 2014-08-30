package eu.timepit.equites
package format

import org.specs2.mutable._

import format.Pgn._
import format.PgnOps._
import util.PieceAbbr.Wiki._
import util.SquareAbbr._

/*
class PgnOpsSpec extends Specification {
  "" should {
    "" in {
      val draw = e2 to e4
      val sanMove = SanMove(Pawn, MaybeDraw(draw))
      ff5(sanMove, GameState.init) must_== Move(pl, draw)
    }

    "" in {
      val sanMove = SanMove(Pawn, MaybeDraw(e4))
      ff5(sanMove, GameState.init) must_== Move(pl, e2 to e4)
    }
  }
}
*/ 