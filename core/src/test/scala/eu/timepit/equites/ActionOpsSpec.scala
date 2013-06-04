// Equites, a Scala chess playground
// Copyright © 2013 Frank S. Thomas <frank@timepit.eu>
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
import util.BoardFactory._
import util.PieceAbbr._

class ActionOpsSpec extends Specification {
  "ActionOps" should {
    val board =
      |>.b.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.P.p.-.
         -.-.p.P.-.-.-.P.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         R.N.-.-.K.-.-.-.<|

    "reconstruct a capture" in {
      val move = Move(R, Square(0, 0), Square(0, 7))
      val capture = Capture(move, b)
      moveAsCapture(board)(move) must beSome(capture)
    }
    "not reconstruct a move as capture" in {
      val move = Move(R, Square(0, 0), Square(0, 6))
      moveAsCapture(board)(move) must beNone
    }
    "not reconstruct an invalid capture" in {
      val move = Move(R, Square(0, 0), Square(1, 0))
      moveAsCapture(board)(move) must beNone
    }

    "reconstruct a white en passant" in {
      val move = Move(P, Square(5, 4), Square(6, 5))
      val enPassant =
        EnPassant(P, Square(5, 4), Square(6, 5), Pawn(Black), Square(6, 4))
      moveAsEnPassant(board)(move) must beSome(enPassant)
    }
    "reconstruct a black en passant" in {
      val move = Move(Pawn(Black), Square(2, 3), Square(3, 2))
      val enPassant =
        EnPassant(Pawn(Black), Square(2, 3), Square(3, 2), P, Square(3, 3))
      moveAsEnPassant(board)(move) must beSome(enPassant)
    }
    "not reconstruct a capture as en passant" in {
      val capture = Capture(P, Square(7, 3), Square(6, 4), Pawn(Black))
      moveAsEnPassant(board)(capture) must beNone
    }

    "reconstruct a long castling" in {
      val move = Move(K, Square(4, 0), Square(2, 0))
      moveAsCastling(move) must beSome(CastlingLong(White))
    }
    "reconstruct a short castling" in {
      val move = Move(K, Square(4, 0), Square(6, 0))
      moveAsCastling(move) must beSome(CastlingShort(White))
    }
  }
}
