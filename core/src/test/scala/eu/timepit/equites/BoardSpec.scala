// Equites, a Scala chess playground
// Copyright © 2011, 2013 Frank S. Thomas <frank@timepit.eu>
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

import util.PieceAbbr.Wiki._
import util.SquareAbbr._

class BoardSpec extends Specification {
  "Board" should {
    val board = Board(a1 -> pl, b2 -> pd)

    "return placed pieces" in {
      board.getPlaced(a1) must beSome(Placed(pl, a1))
      board.getPlaced(b1) must beNone
    }

    "return sequences of placed pieces" in {
      board.getPlaced(Seq(a1, b1, b2)) must_==
        Seq(Placed(pl, a1), Placed(pd, b2))
    }

    "report occupied and vacant squares" in {
      board.isOccupied(a1) must beTrue
      board.isVacant(a1) must beFalse

      board.isVacant(b1) must beTrue
      board.isOccupied(b1) must beFalse
    }

    "report occupied squares by piece" in {
      board.isOccupiedBy(a1, pl) must beTrue
      board.isOccupiedBy(a1, pd) must beFalse
      board.isOccupiedBy(b1, pl) must beFalse
    }

    "return all placed pieces" in {
      board.placedPieces.toSet must_== Set(Placed(pl, a1), Placed(pd, b2))
    }

    def checkAction(before: Board, after: Board, action: Action) = {
      val processed = before.processAction(action)
      val reversed = processed.reverseAction(action)

      processed must_!= before
      processed must_== after
      reversed must_== before
      reversed must_!= after
    }

    "process and reverse moves" in {
      val before = Board(a1 -> ql)
      val after = Board(h8 -> ql)
      val action = Move(ql, a1 to h8)
      checkAction(before, after, action)
    }

    "process and reverse promotions" in {
      val before = Board(a7 -> pl)
      val after = Board(a8 -> ql)
      val action = Promotion(pl, a7 to a8, ql)
      checkAction(before, after, action)
    }

    "process and reverse captures" in {
      val before = Board(a1 -> ql, h8 -> pd)
      val after = Board(h8 -> ql)
      val action = Capture(ql, a1 to h8, pd)
      checkAction(before, after, action)
    }

    "process and reverse captures and promotions" in {
      val before = Board(a7 -> pl, b8 -> nd)
      val after = Board(b8 -> ql)
      val action = CaptureAndPromotion(pl, a7 to b8, nd, ql)
      checkAction(before, after, action)
    }

    "process and reverse castlings" in {
      val castling = CastlingLong(White)
      val before = Board(
        castling.kingMove.draw.src -> castling.king,
        castling.rookMove.draw.src -> castling.rook)
      val after = Board(
        castling.kingMove.draw.dest -> castling.king,
        castling.rookMove.draw.dest -> castling.rook)
      checkAction(before, after, castling)
    }
  }
}
