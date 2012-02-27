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

class ColorSpec extends Specification {
  "object White" should {
    "return Black as opposite Color" in {
      White.opposite must_== Black
    }

    "return White as opposite opposite Color" in {
      White.opposite.opposite must_== White
    }
  }

  "object Black" should {
    "return White as opposite Color" in {
      Black.opposite must_== White
    }

    "return Black as opposite opposite Color" in {
      Black.opposite.opposite must_== Black
    }
  }
}
