// Equites, a Scala chess playground
// Copyright © 2011, 2013-2015 Frank S. Thomas <frank@timepit.eu>
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

import eu.timepit.equites.Rules._
import eu.timepit.equites.util.PieceAbbr.Wiki._
import eu.timepit.equites.util.SquareAbbr._
import org.specs2.mutable._

class RulesSpec extends Specification {
  "Rules" should {
    "determine associated castlings for placed pieces" in {
      associatedCastlings(Placed(rl, a1)) must_== Seq(CastlingLong(White))
      associatedCastlings(Placed(rl, d6)) must_== Seq.empty
      associatedCastlings(Placed(rd, h8)) must_== Seq(CastlingShort(Black))
      associatedCastlings(Placed(kd, e3)) must_== Seq(CastlingShort(Black), CastlingLong(Black))
      associatedCastlings(Placed(ql, a1)) must_== Seq.empty
    }
  }
}
