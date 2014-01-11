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
import util.CoordinateMove
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
      val move = Move(rl, Square(0, 0), Square(0, 7))
      val capture = Capture(move, bd)
      moveAsCapture(board, move) must beSome(capture)
    }
    "not reconstruct a move as capture" in {
      val move = Move(rl, Square(0, 0), Square(0, 6))
      moveAsCapture(board, move) must beNone
    }
    "not reconstruct an invalid capture" in {
      val move = Move(rl, Square(0, 0), Square(1, 0))
      moveAsCapture(board, move) must beNone
    }

    "reconstruct a white en passant" in {
      val move = Move(pl, Square(5, 4), Square(6, 5))
      val enPassant = EnPassant(pl, Square(5, 4), Square(6, 5), pd, Square(6, 4))
      moveAsEnPassant(board, move) must beSome(enPassant)
    }
    "reconstruct a black en passant" in {
      val move = Move(pd, Square(2, 3), Square(3, 2))
      val enPassant = EnPassant(pd, Square(2, 3), Square(3, 2), pl, Square(3, 3))
      moveAsEnPassant(board, move) must beSome(enPassant)
    }
    "not reconstruct a capture as en passant" in {
      val capture = Capture(pl, Square(7, 3), Square(6, 4), pd)
      moveAsEnPassant(board, capture) must beNone
    }

    "reconstruct a long castling" in {
      val move = Move(kl, Square(4, 0), Square(2, 0))
      moveAsCastling(board, move) must beSome(CastlingLong(White))
    }
    "not reconstruct an invalid short castling" in {
      val move = Move(kl, Square(4, 0), Square(6, 0))
      moveAsCastling(board, move) must beNone
    }

    "reconstruct a cm as move" in {
      cmAsAction(board, CoordinateMove(Square(0, 0), Square(0, 6))) must
        beSome(Move(rl, Square(0, 0), Square(0, 6)))
    }
    "reconstruct a cm as promotion" in {
      cmAsAction(board, CoordinateMove(Square(4, 6), Square(4, 7), Some(ql))) must
        beSome(Promotion(pl, Square(4, 6), Square(4, 7), ql))
    }
    "reconstruct a cm as capture" in {
      cmAsAction(board, CoordinateMove(Square(0, 0), Square(0, 7))) must
        beSome(Capture(rl, Square(0, 0), Square(0, 7), bd))
    }
    "reconstruct a cm as capture and promotion" in {
      cmAsAction(board, CoordinateMove(Square(1, 6), Square(0, 7), Some(ql))) must
        beSome(CaptureAndPromotion(pl, Square(1, 6), Square(0, 7), bd, ql))
    }
    "reconstruct a cm as en passant" in {
      cmAsAction(board, CoordinateMove(Square(5, 4), Square(6, 5))) must
        beSome(EnPassant(pl, Square(5, 4), Square(6, 5), pd, Square(6, 4)))
    }
    "reconstruct a cm as long castling" in {
      cmAsAction(board, CoordinateMove(Square(4, 0), Square(2, 0))) must
        beSome(CastlingLong(White))
    }

    "correctly perform isCaptureOrPawnMove for a non-pawn move" in {
      val move = Move(ql, Square(5, 4), Square(6, 5))
      isCaptureOrPawnMove(move) must beFalse
    }
    "correctly perform isCaptureOrPawnMove for a pawn move" in {
      val move = Move(pl, Square(5, 4), Square(6, 5))
      isCaptureOrPawnMove(move) must beTrue
    }
    "correctly perform isCaptureOrPawnMove for a capture" in {
      val capture = Capture(rl, Square(0, 0), Square(0, 7), bd)
      isCaptureOrPawnMove(capture) must beTrue
    }
    "correctly perform isCaptureOrPawnMove for a castling" in {
      val castling = CastlingLong(White)
      isCaptureOrPawnMove(castling) must beFalse
    }
  }
}
