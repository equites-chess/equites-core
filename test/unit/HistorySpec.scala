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

class HistorySpec extends Specification {
  "class History" should {
    val pawn = new Pawn(White)
    val move1 = Move(pawn, Square(0, 0), Square(0, 1))
    val move2 = Move(pawn, Square(0, 1), Square(0, 2))
    val move3 = Move(pawn, Square(0, 2), Square(0, 3))

    "correctly record actions" in {
      var hist = new History
      hist.prevOption must_== None
      hist.nextOption must_== None

      hist = hist.record(move1)
      hist.prevOption must_== Some(move1)
      hist.nextOption must_== None

      hist = hist.record(move2)
      hist.prevOption must_== Some(move2)
      hist.nextOption must_== None

      hist = hist.record(move3)
      hist.prevOption must_== Some(move3)
      hist.nextOption must_== None
    }

    "correctly move backward" in {
      var hist = (new History).record(move1).record(move2).record(move3)
      hist.prevOption must_== Some(move3)
      hist.nextOption must_== None

      hist = hist.backward
      hist.prevOption must_== Some(move2)
      hist.nextOption must_== Some(move3)

      hist = hist.backward
      hist.prevOption must_== Some(move1)
      hist.nextOption must_== Some(move2)

      hist = hist.backward
      hist.prevOption must_== None
      hist.nextOption must_== Some(move1)

      hist = hist.backward
      hist.prevOption must_== None
      hist.nextOption must_== Some(move1)
    }

    "correctly move forward" in {
      var hist = (new History).record(move1).record(move2).record(move3)
        .backward.backward.backward
      hist.prevOption must_== None
      hist.nextOption must_== Some(move1)

      hist = hist.forward
      hist.prevOption must_== Some(move1)
      hist.nextOption must_== Some(move2)

      hist = hist.forward
      hist.prevOption must_== Some(move2)
      hist.nextOption must_== Some(move3)

      hist = hist.forward
      hist.prevOption must_== Some(move3)
      hist.nextOption must_== None

      hist = hist.forward
      hist.prevOption must_== Some(move3)
      hist.nextOption must_== None
    }

    "correctly record actions in the past" in {
      var hist = (new History).record(move1).record(move2).record(move3)
        .backward.backward
      hist.prevOption must_== Some(move1)
      hist.nextOption must_== Some(move2)

      hist = hist.record(move3)
      hist.past       must_== List(move3, move1)
      hist.prevOption must_== Some(move3)
      hist.nextOption must_== None
    }
  }
}
