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

import ActionOps._
import util.BoardBuilder._
import util.CoordinateAction
import util.PieceAbbr.Wiki._

class ActionOpsSpec extends Specification {
  "ActionOps" should {
    val board =
      >>.b.|.|.|.|.|.|.|.
        |.P.|.|.P.|.|.|.
        |.|.|.|.|.|.|.|.
        |.|.|.|.|.P.p.|.
        |.|.p.P.|.|.|.P.
        |.|.|.|.|.|.|.|.
        |.|.|.|.|.|.|.|.
        R.N.|.|.K.|.|.|.<<

    "reconstruct a capture" in {
      val move = Move(rl, Square.unsafeFrom(0, 0) to Square.unsafeFrom(0, 7))
      val capture = Capture(move.piece, move.draw, bd)
      reifyAsCapture(board, move) must beSome(capture)
    }
    "not reconstruct a move as capture" in {
      val move = Move(rl, Square.unsafeFrom(0, 0) to Square.unsafeFrom(0, 6))
      reifyAsCapture(board, move) must beNone
    }
    "not reconstruct an invalid capture" in {
      val move = Move(rl, Square.unsafeFrom(0, 0) to Square.unsafeFrom(1, 0))
      reifyAsCapture(board, move) must beNone
    }

    "reconstruct a white en passant" in {
      val move = Move(pl, Square.unsafeFrom(5, 4) to Square.unsafeFrom(6, 5))
      val enPassant = EnPassant(pl, Square.unsafeFrom(5, 4) to Square.unsafeFrom(6, 5), pd, Square.unsafeFrom(6, 4))
      reifyAsEnPassant(board, move) must beSome(enPassant)
    }
    "reconstruct a black en passant" in {
      val move = Move(pd, Square.unsafeFrom(2, 3) to Square.unsafeFrom(3, 2))
      val enPassant = EnPassant(pd, Square.unsafeFrom(2, 3) to Square.unsafeFrom(3, 2), pl, Square.unsafeFrom(3, 3))
      reifyAsEnPassant(board, move) must beSome(enPassant)
    }
    "not reconstruct a capture as en passant" in {
      val capture = Capture(pl, Square.unsafeFrom(7, 3) to Square.unsafeFrom(6, 4), pd)
      reifyAsEnPassant(board, capture) must beNone
    }

    "reconstruct a long castling" in {
      val move = Move(kl, Square.unsafeFrom(4, 0) to Square.unsafeFrom(2, 0))
      reifyAsCastling(board, move) must beSome(CastlingLong(White))
    }
    "not reconstruct an invalid short castling" in {
      val move = Move(kl, Square.unsafeFrom(4, 0) to Square.unsafeFrom(6, 0))
      reifyAsCastling(board, move) must beNone
    }

    "reconstruct a cm as move" in {
      reifyAsAction(board, CoordinateAction(Square.unsafeFrom(0, 0) to Square.unsafeFrom(0, 6))) must
        beSome(Move(rl, Square.unsafeFrom(0, 0) to Square.unsafeFrom(0, 6)))
    }
    "reconstruct a cm as promotion" in {
      reifyAsAction(board, CoordinateAction(Square.unsafeFrom(4, 6) to Square.unsafeFrom(4, 7), Some(ql))) must
        beSome(Promotion(pl, Square.unsafeFrom(4, 6) to Square.unsafeFrom(4, 7), ql))
    }
    "reconstruct a cm as capture" in {
      reifyAsAction(board, CoordinateAction(Square.unsafeFrom(0, 0) to Square.unsafeFrom(0, 7))) must
        beSome(Capture(rl, Square.unsafeFrom(0, 0) to Square.unsafeFrom(0, 7), bd))
    }
    "reconstruct a cm as capture and promotion" in {
      reifyAsAction(board, CoordinateAction(Square.unsafeFrom(1, 6) to Square.unsafeFrom(0, 7), Some(ql))) must
        beSome(CaptureAndPromotion(pl, Square.unsafeFrom(1, 6) to Square.unsafeFrom(0, 7), bd, ql))
    }
    "reconstruct a cm as en passant" in {
      reifyAsAction(board, CoordinateAction(Square.unsafeFrom(5, 4) to Square.unsafeFrom(6, 5))) must
        beSome(EnPassant(pl, Square.unsafeFrom(5, 4) to Square.unsafeFrom(6, 5), pd, Square.unsafeFrom(6, 4)))
    }
    "reconstruct a cm as long castling" in {
      reifyAsAction(board, CoordinateAction(Square.unsafeFrom(4, 0) to Square.unsafeFrom(2, 0))) must
        beSome(CastlingLong(White))
    }

    "correctly perform isCaptureOrPawnMove for a non-pawn move" in {
      val move = Move(ql, Square.unsafeFrom(5, 4) to Square.unsafeFrom(6, 5))
      isCaptureOrPawnMove(move) must beFalse
    }
    "correctly perform isCaptureOrPawnMove for a pawn move" in {
      val move = Move(pl, Square.unsafeFrom(5, 4) to Square.unsafeFrom(6, 5))
      isCaptureOrPawnMove(move) must beTrue
    }
    "correctly perform isCaptureOrPawnMove for a capture" in {
      val capture = Capture(rl, Square.unsafeFrom(0, 0) to Square.unsafeFrom(0, 7), bd)
      isCaptureOrPawnMove(capture) must beTrue
    }
    "correctly perform isCaptureOrPawnMove for a castling" in {
      val castling = CastlingLong(White)
      isCaptureOrPawnMove(castling) must beFalse
    }
  }
}
