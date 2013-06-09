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

object Uci {
  sealed trait Command extends util.TextCommand
  sealed trait Request extends Command
  sealed trait Response extends Command

  // Requests to the engine:

  case object Uci extends Request

  case class Debug(on: Boolean) extends Request {
    override def toString: String = cmdName + " " + util.toStringOnOff(on)
  }

  case object IsReady extends Request

  // TODO: setoption

  // TODO: register

  case object UciNewGame extends Request

  // TODO: position

  // TODO: go

  case object Stop extends Request

  case object PonderHit extends Request

  case object Quit extends Request

  // Responses from the engine:

  case class Id(key: String, value: String) extends Response

  case object UciOk extends Response

  case object ReadyOk extends Response

  case class Bestmove(move: util.CoordinateMove,
    ponder: Option[util.CoordinateMove] = None) extends Response {

    override def toString: String = cmdName + " " + move.toAlgebraic +
      ponder.map(" ponder " + _.toAlgebraic).getOrElse("")
  }

  // TODO: copyprotection

  // TODO: registration

  // TODO: info

  // TODO: option
}
