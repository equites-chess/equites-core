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
import org.specs2.matcher.ParserMatchers

class PGNParsersSpec extends Specification with ParserMatchers {
  val parsers = PGNParsers
  import parsers._

  "moveNumberIndicator" should {
    "succeed on" in {
      moveNumberIndicator must succeedOn("23.")
      moveNumberIndicator must succeedOn("42...")
    }

    "fail on" in {
      moveNumberIndicator must failOn("1")
      moveNumberIndicator must failOn("2..")
      moveNumberIndicator must failOn("a3.")
      moveNumberIndicator must failOn("4....")
    }
  }
}
