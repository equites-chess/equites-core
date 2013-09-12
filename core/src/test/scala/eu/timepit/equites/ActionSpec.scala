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

import util.PieceAbbr.Wiki._

class ActionSpec extends Specification {
  "Move should be constructible from Placed" in {
    Move(pl, Square('e', 2), Square('e', 4)) must_==
      Move(Placed(pl, Square('e', 2)), Square('e', 4))
  }
  "Promotion should be constructible from Placed" in {
    Promotion(pl, Square('e', 7), Square('e', 8), ql) must_==
      Promotion(Placed(pl, Square('e', 7)), Square('e', 8), ql)
  }
  "Capture should be constructible from Placed" in {
    Capture(pl, Square('e', 2), Square('f', 3), bd) must_==
      Capture(Placed(pl, Square('e', 2)), Square('f', 3), bd)
  }
  "CaptureAndPromotion should be constructible from Placed" in {
    CaptureAndPromotion(pl, Square('e', 7), Square('f', 8), bd, ql) must_==
      CaptureAndPromotion(Placed(pl, Square('e', 7)), Square('f', 8), bd, ql)
  }
  "EnPassant should be constructible from Placed" in {
    EnPassant(pl, Square(5, 4), Square(6, 5), pd, Square(6, 4)) must_==
      EnPassant(Placed(pl, Square(5, 4)), Square(6, 5), pd, Square(6, 4))
  }
}
