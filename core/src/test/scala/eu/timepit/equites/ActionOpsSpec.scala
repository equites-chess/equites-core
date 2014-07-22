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

import org.specs2.mutable._

import util.BoardBuilder._
import util.CoordinateAction
import util.PieceAbbr.Wiki._
import util.SquareAbbr._
import ActionOps._

class ActionOpsSpec extends Specification {
  "ActionOps" should {
    val board = >>.
      b.|.|.|.|.|.|.|.
      |.P.|.|.P.|.|.|.
      |.|.|.|.|.|.|.|.
      |.|.|.|.|.P.p.|.
      |.|.p.P.|.|.|.P.
      |.|.|.|.|.|.|.|.
      |.|.|.|.|.|.|.|.
      R.N.|.|.K.|.|.|.<<

    "reconstruct a capture" in {
      val move = Move(rl, a1 to a8)
      val capture = Capture(move.piece, move.draw, bd)
      reifyAsCapture(board, move) must beSome(capture)
    }
    "not reconstruct a move as capture" in {
      val move = Move(rl, a1 to a7)
      reifyAsCapture(board, move) must beNone
    }
    "not reconstruct an invalid capture" in {
      val move = Move(rl, a1 to b1)
      reifyAsCapture(board, move) must beNone
    }

    "reconstruct a white en passant" in {
      val move = Move(pl, f5 to g6)
      val enPassant = EnPassant(pl, f5 to g6, pd, g5)
      reifyAsEnPassant(board, move) must beSome(enPassant)
    }
    "reconstruct a black en passant" in {
      val move = Move(pd, c4 to d3)
      val enPassant = EnPassant(pd, c4 to d3, pl, d4)
      reifyAsEnPassant(board, move) must beSome(enPassant)
    }
    "not reconstruct a capture as en passant" in {
      val capture = Capture(pl, h4 to g5, pd)
      reifyAsEnPassant(board, capture) must beNone
    }

    "reconstruct a long castling" in {
      val move = Move(kl, e1 to c1)
      reifyAsCastling(board, move) must beSome(CastlingLong(White))
    }
    "not reconstruct an invalid short castling" in {
      val move = Move(kl, e1 to g1)
      reifyAsCastling(board, move) must beNone
    }

    "reconstruct a cm as move" in {
      reifyAsAction(board, CoordinateAction(a1 to a7)) must
        beSome(Move(rl, a1 to a7))
    }
    "reconstruct a cm as promotion" in {
      reifyAsAction(board, CoordinateAction(e7 to e8, Some(ql))) must
        beSome(Promotion(pl, e7 to e8, ql))
    }
    "reconstruct a cm as capture" in {
      reifyAsAction(board, CoordinateAction(a1 to a8)) must
        beSome(Capture(rl, a1 to a8, bd))
    }
    "reconstruct a cm as capture and promotion" in {
      reifyAsAction(board, CoordinateAction(b7 to a8, Some(ql))) must
        beSome(CaptureAndPromotion(pl, b7 to a8, bd, ql))
    }
    "reconstruct a cm as en passant" in {
      reifyAsAction(board, CoordinateAction(f5 to g6)) must
        beSome(EnPassant(pl, f5 to g6, pd, g5))
    }
    "reconstruct a cm as long castling" in {
      reifyAsAction(board, CoordinateAction(e1 to c1)) must
        beSome(CastlingLong(White))
    }

    "correctly perform isCaptureOrPawnMove for a non-pawn move" in {
      val move = Move(ql, f5 to g6)
      isCaptureOrPawnMove(move) must beFalse
    }
    "correctly perform isCaptureOrPawnMove for a pawn move" in {
      val move = Move(pl, f5 to g6)
      isCaptureOrPawnMove(move) must beTrue
    }
    "correctly perform isCaptureOrPawnMove for a capture" in {
      val capture = Capture(rl, a1 to a8, bd)
      isCaptureOrPawnMove(capture) must beTrue
    }
    "correctly perform isCaptureOrPawnMove for a castling" in {
      val castling = CastlingLong(White)
      isCaptureOrPawnMove(castling) must beFalse
    }
  }
}
