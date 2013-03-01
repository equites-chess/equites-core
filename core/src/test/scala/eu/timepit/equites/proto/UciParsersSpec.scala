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
package proto

import org.specs2.mutable._
import org.specs2.matcher.ParserMatchers

import Uci._

class UciParsersSpec extends Specification with ParserMatchers  {
  val parsers = UciParsers
  import parsers._

  "id" should {
    "succeed on valid input" in {
      id should succeedOn("id name Engine X Y")
        .withResult(Id("name", "Engine X Y"))
      id should succeedOn("id author John Doe")
        .withResult(Id("author", "John Doe"))
    }
    "succeed on empty value" in {
      id should succeedOn("id name").withResult(Id("name", ""))
    }
    "fail on missing key" in {
      id should failOn("id")
    }
  }
}
