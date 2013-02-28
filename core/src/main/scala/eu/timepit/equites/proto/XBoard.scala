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

import implicits.ActionImplicits._

// http://www.gnu.org/software/xboard/engine-intf.html
object XBoard {
  sealed abstract class Version(version: Int) {
    override def toString: String = version.toString
  }

  case object Version2 extends Version(2)

  sealed trait Command {
    self: Product =>

    override def toString: String = {
      val cmd = util.getClassName(this).toLowerCase
      val args = productIterator.mkString(" ")
      if (args.isEmpty) cmd else cmd + " " + args
    }
  }

  case object XBoard extends Command

  case class ProtoVer(version: Version = Version2) extends Command

  case object Accepted extends Command

  case object Rejected extends Command

  case object New extends Command

  case object Quit extends Command

  case object Random extends Command

  case object Force extends Command

  case object Go extends Command

  case object PlayOther extends Command

  case class Time(centiseconds: Int) extends Command

  case class OTim(centiseconds: Int) extends Command

  case class UserMove(move: Action) extends Command {
    override def toString: String = "usermove " + move.toCoordinate
  }

  case object MoveNow extends Command {
    override def toString: String = "?"
  }

  case class Ping(n: Int) extends Command

  case object Draw extends Command

  case class Result(result: eu.timepit.equites.Result) extends Command

  // TODO: Board => FEN string
  case class SetBoard(board: Board) extends Command

  // TODO: edit

  case object Hint extends Command

  case object Bk extends Command

  case object Undo extends Command

  case object Remove extends Command

  case object Hard extends Command

  case object Easy extends Command

  case object Post extends Command

  case object NoPost extends Command

  case class Name(name: String) extends Command

  // TODO: rating

  case class Hostname(host: String) extends Command

  case object Computer extends Command

  case object Pause extends Command

  case object Resume extends Command

  case class Memory(n: Int) extends Command {
    require(n >= 0)
  }

  case class Cores(n: Int) extends Command {
    require(n >= 0)
  }

  // TODO: egtpath

  // TODO: option
}
