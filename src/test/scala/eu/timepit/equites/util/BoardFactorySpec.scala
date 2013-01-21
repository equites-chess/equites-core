// Equites, a simple chess interface
// Copyright © 2013 Frank S. Thomas <f.thomas@gmx.de>
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
package util

import org.specs2.mutable._

import BoardFactory._

class BoardFactorySpec extends Specification {
  "BoardFactory" should {
    "correctly construct empty boards" in {
      |>.<| must_== Board()
      |>.-.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.<| must_== Board()
    }
    "correctly construct non-empty boards" in {
      |>.r.n.b.q.k.b.n.r.
         p.p.p.p.p.p.p.p.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         -.-.-.-.-.-.-.-.
         P.P.P.P.P.P.P.P.
         R.N.B.Q.K.B.N.R.<| must_== Rules.startingBoard
    }
  }
}
