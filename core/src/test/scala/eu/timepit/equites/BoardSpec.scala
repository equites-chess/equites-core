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

class BoardSpec extends Specification {
  "Board" should {
    "report occupied squares" in {
      val board = Board(Square(0, 0) -> Pawn(White),
                        Square(1, 1) -> Pawn(Black))

      board.isOccupied(Square(0, 0)) must beTrue
      board.isOccupied(Square(1, 1)) must beTrue

      board.isOccupied(Square(1, 0)) must beFalse
      board.isOccupied(Square(0, 1)) must beFalse

      board.isOccupiedBy(Square(0, 0), Pawn(White)) must beTrue
      board.isOccupiedBy(Square(1, 1), Pawn(White)) must beFalse

      board.isOccupiedBy(Square(0, 0), Pawn(Black)) must beFalse
      board.isOccupiedBy(Square(1, 1), Pawn(Black)) must beTrue

      board.isOccupiedBy(Square(0, 1), Pawn(White)) must beFalse
      board.isOccupiedBy(Square(1, 0), Pawn(Black)) must beFalse
    }

    def checkAction(before: Board, after: Board, action: Action) = {
      before.processAction(action) must_!= before
      before.processAction(action) must_== after

      before.processAction(action).reverseAction(action) must_== before
      before.processAction(action).reverseAction(action) must_!= after
    }

    "correctly process/reverse Moves" in {
      val before = Board(Square(0, 0) -> Queen(White))
      val after  = Board(Square(7, 7) -> Queen(White))
      val action = Move(Queen(White), Square(0, 0), Square(7, 7))

      checkAction(before, after, action)
    }

    "correctly process/reverse Promotions" in {
      val before = Board(Square(0, 6) -> Pawn(White))
      val after  = Board(Square(0, 7) -> Queen(White))
      val action =
        Promotion(Pawn(White), Square(0, 6), Square(0, 7), Queen(White))

      checkAction(before, after, action)
    }

    "correctly process/reverse Captures" in {
      val before = Board(Square(0, 0) -> Queen(White),
                         Square(7, 7) -> Pawn(Black))
      val after  = Board(Square(7, 7) -> Queen(White))
      val action =
        Capture(Queen(White), Square(0, 0), Square(7, 7), Pawn(Black))

      checkAction(before, after, action)
    }
  }
}
