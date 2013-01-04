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

package eu.timepit.equites

import org.specs2.mutable._

/*
class BoardSpec extends Specification {
  "class Board" should {
    "correctly report occupied squares" in {
      val board = new Board
      val piece1 = new Pawn(White)
      val piece2 = new Pawn(White)
      val piece3 = new Pawn(Black)

      board.contains(piece1)       must_== false
      board.occupied(Square(0, 0)) must_== false
      board.occupiedBy(Square(0, 0), piece1) must_== false

      board.putPiece(Square(0, 0), piece1)
      board.contains(piece1)       must_== true
      board.occupied(Square(0, 0)) must_== true
      board.occupiedBy(Square(0, 0), piece1) must_== true
      board.occupiedBy(Square(0, 0), piece2) must_== false
      board.opponentAt(Square(0, 0), piece2.color) must_== false
      board.opponentAt(Square(0, 0), piece3.color) must_== true

      board.putPiece(Square(1, 1), piece2)
      board.occupiedBy(Square(0, 0), piece1) must_== true
      board.occupiedBy(Square(1, 1), piece2) must_== true
    }

    "correctly perform getter/setter methods" in {
      val board = new Board
      val piece1 = new Pawn(White)
      val piece2 = new Pawn(White)
      val piece3 = new Pawn(Black)

      board.getPiece(Square(0, 0)) must_== None
      board.getSquare(piece1)      must_== None

      board.putPiece(Square(0, 0), piece1)
      board.getPiece(Square(0, 0)).get must_== piece1
      board.getSquare(piece1).get      must_== Square(0, 0)

      board.contains(piece1) must_== true
      board.contains(piece2) must_== false
      board.contains(piece3) must_== false

      board.putPieces(Seq((Square(1, 1), piece2), (Square(2, 2), piece3)))
      board.contains(piece1) must_== true
      board.contains(piece2) must_== true
      board.contains(piece3) must_== true

      board.removePiece(Square(2, 2)).get must_== piece3
      board.getPiece(Square(2, 2))  must_== None
      board.contains(piece3)        must_== false

      board.removePiece(piece2).get must_== Square(1, 1)
      board.getPiece(Square(1, 1))  must_== None
      board.contains(piece2)        must_== false

      board.putPieces(Seq((Square(1, 1), piece2), (Square(2, 2), piece3)))
      board.contains(piece1) must_== true
      board.contains(piece2) must_== true
      board.contains(piece3) must_== true

      board.removePieces(Seq(piece2, piece3))
      board.contains(piece1) must_== true
      board.contains(piece2) must_== false
      board.contains(piece3) must_== false
      board.isEmpty          must_== false

      board.clear()
      board.contains(piece1) must_== false
      board.isEmpty          must_== true
    }

    "correctly perform/reverse moves" in {
      val board = new Board
      val piece = new Pawn(White)
      val from = Square(0, 0)
      val to   = Square(1, 0)
      val move = Move(piece, from, to)

      board.putPiece(from, piece)
      board.occupiedBy(from, piece) must_== true
      board.occupied(to)            must_== false

      board.processAction(move)
      board.occupied(from)          must_== false
      board.occupiedBy(to, piece)   must_== true

      board.reverseAction(move)
      board.occupiedBy(from, piece) must_== true
      board.occupied(to)            must_== false
    }
  }
}
*/