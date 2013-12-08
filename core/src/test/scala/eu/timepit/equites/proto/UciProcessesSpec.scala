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

import org.specs2._
import scalaz.stream._

import UciProcesses._

class UciProcessesSpec extends Specification { def is = s2"""
  UciProcesses
    collectResponses should parse and filter Uci.Response $e1
  """

  def e1 = {
    val input = Process("uciok", "foo bar", "id author John", "baz")
    val result = List(Uci.UciOk, Uci.Id("author", "John"))
    input.pipe(collectResponses).toList must_== result
  }
}
