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
package util

import org.specs2.ScalaCheck
import org.specs2.mutable._

class PackageSpec extends Specification {
  "backtracking" should {
    "generate all 'binary' strings of length three" in {
      backtracking("")(
          c => Stream("0", "1").map(c + _),
          _.length == 3).toSet must_==
        Set("000", "001", "010", "011", "100", "101", "110", "111")
    }
  }
}
