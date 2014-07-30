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
      reifyCapture(move, board) must beSome(capture)
    }
    "not reconstruct a move as capture" in {
      val move = Move(rl, a1 to a7)
      reifyCapture(move, board) must beNone
    }
    "not reconstruct an invalid capture" in {
      val move = Move(rl, a1 to b1)
      reifyCapture(move, board) must beNone
    }

    "reconstruct a white en passant" in {
      val move = Move(pl, f5 to g6)
      val enPassant = EnPassant(pl, f5 to g6, pd, g5)
      reifyEnPassant(move, board) must beSome(enPassant)
    }
    "reconstruct a black en passant" in {
      val move = Move(pd, c4 to d3)
      val enPassant = EnPassant(pd, c4 to d3, pl, d4)
      reifyEnPassant(move, board) must beSome(enPassant)
    }
    "not reconstruct a capture as en passant" in {
      val capture = Capture(pl, h4 to g5, pd)
      reifyEnPassant(capture, board) must beNone
    }

    "reconstruct a long castling" in {
      val move = Move(kl, e1 to c1)
      reifyCastling(move, board) must beSome(CastlingLong(White))
    }
    "not reconstruct an invalid short castling" in {
      val move = Move(kl, e1 to g1)
      reifyCastling(move, board) must beNone
    }

    "reconstruct a cm as move" in {
      reifyAction(CoordinateAction(a1 to a7), board) must
        beSome(Move(rl, a1 to a7))
    }
    "reconstruct a cm as promotion" in {
      reifyAction(CoordinateAction(e7 to e8, Some(ql)), board) must
        beSome(Promotion(pl, e7 to e8, ql))
    }
    "reconstruct a cm as capture" in {
      reifyAction(CoordinateAction(a1 to a8), board) must
        beSome(Capture(rl, a1 to a8, bd))
    }
    "reconstruct a cm as capture and promotion" in {
      reifyAction(CoordinateAction(b7 to a8, Some(ql)), board) must
        beSome(CaptureAndPromotion(pl, b7 to a8, bd, ql))
    }
    "reconstruct a cm as en passant" in {
      reifyAction(CoordinateAction(f5 to g6), board) must
        beSome(EnPassant(pl, f5 to g6, pd, g5))
    }
    "reconstruct a cm as long castling" in {
      reifyAction(CoordinateAction(e1 to c1), board) must
        beSome(CastlingLong(White))
    }
  }
}
