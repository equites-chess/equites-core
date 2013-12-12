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

import scalaz.concurrent.Task
import scalaz.stream._

import Uci._
import UciParsers._

object UciProcesses {
  def toRawCommands[A](as: A*): Process[Task, Array[Byte]] =
    Process(as: _*).map(util.toUtf8BytesLf)

  def collectFirst[I, I2](pf: PartialFunction[I, I2]): Process1[I, I2] =
    process1.collect(pf).take(1)

  def collectResponses: Process1[String, Response] =
    process1
    .lift((s: String) => parseAll(response, s))
    .collect { case Success(result, _) => result }
}
