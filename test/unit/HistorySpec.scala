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
      val hist = new History
      val move1 = Move(new Pawn(White), Field(0, 0), Field(0, 1))
      val move2 = Move(new Pawn(White), Field(1, 0), Field(1, 1))
      val move3 = Move(new Pawn(White), Field(2, 0), Field(2, 1))

      hist.hasLast must_== false
      hist.hasNext must_== false

      hist.record(move1)
      hist.hasLast  must_== true
      hist.hasNext  must_== false
      hist.last.get must_== move1

      hist.record(move2)
      hist.hasLast  must_== true
      hist.hasNext  must_== false
      hist.last.get must_== move2

      hist.backward().get must_== move2
      hist.hasLast  must_== true
      hist.hasNext  must_== true
      hist.last.get must_== move1
      hist.next.get must_== move2

      hist.backward().get must_== move1
      hist.hasLast  must_== false
      hist.hasNext  must_== true
      hist.next.get must_== move1

      hist.forward().get must_== move1
      hist.hasLast  must_== true
      hist.hasNext  must_== true
      hist.last.get must_== move1
      hist.next.get must_== move2

      hist.record(move3)
      hist.hasLast  must_== true
      hist.hasNext  must_== false
      hist.last.get must_== move3

      hist.backward()
      hist.hasLast  must_== true
      hist.hasNext  must_== true
      hist.last.get must_== move1
      hist.next.get must_== move3

      hist.clear()
      hist.hasLast  must_== false
      hist.hasNext  must_== false
    }
  }
}
