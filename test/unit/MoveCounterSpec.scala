// Equites, a simple chess interface
// Copyright © 2011 Frank S. Thomas <f.thomas@gmx.de>
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

package equites

import org.specs2.mutable._

class MoveCounterSpec extends Specification {
  "class MoveCounter" should {
    "correctly count normal moves" in {
      val piece1 = new King(White)
      val move1 = Move(piece1, Field(0, 0), Field(1, 1))
      val move2 = Move(piece1, Field(1, 1), Field(2, 2))
      val mc = new MoveCounter

      mc.totalMoves(piece1) must throwAn[IllegalArgumentException]
      mc.hasMoved(piece1)   must throwAn[IllegalArgumentException]

      mc.addPiece(piece1)
      mc.totalMoves(piece1) must_== 0
      mc.hasMoved(piece1)   must_== false

      mc.processAction(move1)
      mc.totalMoves(piece1) must_== 1
      mc.hasMoved(piece1)   must_== true

      mc.processAction(move2)
      mc.totalMoves(piece1) must_== 2
      mc.hasMoved(piece1)   must_== true

      mc.reverseAction(move2)
      mc.totalMoves(piece1) must_== 1
      mc.hasMoved(piece1)   must_== true

      mc.reverseAction(move1)
      mc.totalMoves(piece1) must_== 0
      mc.hasMoved(piece1)   must_== false

      mc.removePiece(piece1)
      mc.totalMoves(piece1) must throwAn[IllegalArgumentException]
      mc.hasMoved(piece1)   must throwAn[IllegalArgumentException]
    }

    "correctly add and remove pieces" in {
      val mc = new MoveCounter
      val piece1 = new King(White)
      val piece2 = new Rook(White)
      val piece3 = new Rook(White)

      mc.hasMoved(piece1) must throwAn[IllegalArgumentException]
      mc.hasMoved(piece2) must throwAn[IllegalArgumentException]
      mc.hasMoved(piece3) must throwAn[IllegalArgumentException]

      mc.addPiece(piece1)
      mc.addPieces(Seq(piece2, piece3))

      mc.hasMoved(piece1) must_== false
      mc.hasMoved(piece2) must_== false
      mc.hasMoved(piece3) must_== false

      mc.removePiece(piece3)
      mc.removePieces(List(piece2, piece1))

      mc.hasMoved(piece1) must throwAn[IllegalArgumentException]
      mc.hasMoved(piece2) must throwAn[IllegalArgumentException]
      mc.hasMoved(piece3) must throwAn[IllegalArgumentException]
    }

    "fail on negative move counts" in {
      val mc = new MoveCounter
      val piece1 = new King(White)
      val move1 = Move(piece1, Field(0, 0), Field(1, 1))

      mc.addPiece(piece1)
      mc.totalMoves(piece1)   must_== 0
      mc.reverseAction(move1) must throwAn[IllegalArgumentException]
    }
  }
}
