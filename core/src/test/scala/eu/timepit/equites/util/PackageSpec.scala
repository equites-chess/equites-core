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

import org.specs2.mutable._

class PackageSpec extends Specification {
  "backtracking" should {
    "generate all 'binary' strings of length three" in {
      backtracking("")(c => Stream("0", "1").map(_ + c), _.length == 3)
          .toSet must_==
        Set("000", "001", "010", "011", "100", "101", "110", "111")
    }

    "generate all odd 'binary' strings of length three" in {
      backtracking("1")(c => Stream("0", "1").map(_ + c), _.length == 3)
          .toSet must_==
        Set("001", "011", "101", "111")
    }
  }

  "toUtf8" should {
    "return byte sequences of right length" in {
      toUtf8(0x0000.toChar).length must_== 1
      toUtf8(0x007F.toChar).length must_== 1

      toUtf8(0x0080.toChar).length must_== 2
      toUtf8(0x07FF.toChar).length must_== 2

      toUtf8(0x0800.toChar).length must_== 3
      toUtf8(0xFFFF.toChar).length must_== 3
    }
  }
}
