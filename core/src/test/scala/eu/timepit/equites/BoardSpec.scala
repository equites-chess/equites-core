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

class BoardSpec extends Specification {
  "Board" should {
    val board = Board(Square(0, 0) -> pl, Square(1, 1) -> pd)

    "return placed pieces" in {
      board.getPlaced(Square(0, 0)) must beSome(Placed(pl, Square(0, 0)))
      board.getPlaced(Square(1, 0)) must beNone
    }

    "return sequences of placed pieces" in {
      board.getPlaced(Seq(Square(0, 0), Square(1, 0), Square(1, 1))) must_==
        Seq(Placed(pl, Square(0, 0)), Placed(pd, Square(1, 1)))
    }

    "report occupied and vacant squares" in {
      board.isOccupied(Square(0, 0)) must beTrue
      board.isVacant(Square(0, 0)) must beFalse

      board.isVacant(Square(1, 0)) must beTrue
      board.isOccupied(Square(1, 0)) must beFalse
    }

    "report occupied squares by piece" in {
      board.isOccupiedBy(Square(0, 0), pl) must beTrue
      board.isOccupiedBy(Square(0, 0), pd) must beFalse

      board.isOccupiedBy(Square(1, 0), pl) must beFalse
    }

    "return all placed pieces" in {
      board.placedPieces.toSet must_==
        Set(Placed(pl, Square(0, 0)), Placed(pd, Square(1, 1)))
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
      val before = Board(Square(0, 0) -> ql)
      val after = Board(Square(7, 7) -> ql)
      val action = Move(ql, Square(0, 0), Square(7, 7))
      checkAction(before, after, action)
    }

    "process and reverse promotions" in {
      val before = Board(Square(0, 6) -> pl)
      val after = Board(Square(0, 7) -> ql)
      val action = Promotion(pl, Square(0, 6), Square(0, 7), ql)
      checkAction(before, after, action)
    }

    "process and reverse captures" in {
      val before = Board(Square(0, 0) -> ql, Square(7, 7) -> pd)
      val after = Board(Square(7, 7) -> ql)
      val action = Capture(ql, Square(0, 0), Square(7, 7), pd)
      checkAction(before, after, action)
    }

    "process and reverse captures and promotions" in {
      val before = Board(Square(0, 6) -> pl, Square(1, 7) -> nd)
      val after = Board(Square(1, 7) -> ql)
      val action = CaptureAndPromotion(pl, Square(0, 6), Square(1, 7), nd, ql)
      checkAction(before, after, action)
    }

    "process and reverse castlings" in {
      val castling = CastlingLong(White)
      val before = Board(castling.kingMove.from -> castling.king,
        castling.rookMove.from -> castling.rook)
      val after = Board(castling.kingMove.to -> castling.king,
        castling.rookMove.to -> castling.rook)
      checkAction(before, after, castling)
    }
  }
}
