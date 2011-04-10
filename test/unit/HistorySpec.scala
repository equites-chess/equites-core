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
    "correctly record actions" in {
      var hist = new History
      val move1 = Move(new Pawn(White), Square(0, 0), Square(0, 1))
      val move2 = Move(new Pawn(White), Square(1, 0), Square(1, 1))
      val move3 = Move(new Pawn(White), Square(2, 0), Square(2, 1))

      hist.hasPrev must_== false
      hist.hasNext must_== false
      hist.prev    must_== None
      hist.next    must_== None
      hist.past    must_== List()
      hist.future  must_== List()

      hist = hist.record(move1)
      hist.hasPrev must_== true
      hist.hasNext must_== false
      hist.prev    must_== Some(move1)
      hist.next    must_== None
      hist.past    must_== List(move1)
      hist.future  must_== List()

      hist = hist.record(move2)
      hist.hasPrev must_== true
      hist.hasNext must_== false
      hist.prev    must_== Some(move2)
      hist.next    must_== None
      hist.past    must_== List(move2, move1)
      hist.future  must_== List()

      hist = hist.backward
      hist.hasPrev must_== true
      hist.hasNext must_== true
      hist.prev    must_== Some(move1)
      hist.next    must_== Some(move2)
      hist.past    must_== List(move1)
      hist.future  must_== List(move2)

      hist = hist.backward
      hist.hasPrev must_== false
      hist.hasNext must_== true
      hist.prev    must_== None
      hist.next    must_== Some(move1)
      hist.past    must_== List()
      hist.future  must_== List(move1, move2)

      hist = hist.forward
      hist.hasPrev must_== true
      hist.hasNext must_== true
      hist.prev    must_== Some(move1)
      hist.next    must_== Some(move2)
      hist.past    must_== List(move1)
      hist.future  must_== List(move2)

      hist = hist.record(move3)
      hist.hasPrev must_== true
      hist.hasNext must_== false
      hist.prev    must_== Some(move3)
      hist.next    must_== None
      hist.past    must_== List(move3, move1)
      hist.future  must_== List()

      hist = hist.backward
      hist.hasPrev must_== true
      hist.hasNext must_== true
      hist.prev    must_== Some(move1)
      hist.next    must_== Some(move3)
      hist.past    must_== List(move1)
      hist.future  must_== List(move3)

      hist = hist.backward
      hist = hist.backward
      hist.prev    must_== None
      hist.past    must_== List()
      hist.future  must_== List(move1, move3)

      hist = hist.forward
      hist = hist.forward
      hist = hist.forward
      hist.next    must_== None
      hist.past    must_== List(move3, move1)
      hist.future  must_== List()
    }
  }
}
