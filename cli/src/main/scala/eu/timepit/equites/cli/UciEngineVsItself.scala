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
package cli

import scalaz.concurrent.Task
import scalaz.stream._

import proto.Uci._
import proto.UciProcess._
import util.ScalazProcess._

object UciEngineVsItself extends App {

  type SimpleHistory = Vector[GameState]

  /* TODO
   * - better names for read write
   * - inputPipe
   * - outputPipe
   * - errorPipe
   * - proc.input
   * - proc.output
   * - proc.error
   * Subprocess
   * popen
  */
  
  val (proc, write, read) = ProgramProcesses.system("gnuchess", "-u")
  //val (proc, write, read) = ProgramProcesses.system("fruit")

  def writeGo = toRawCommands(Go(Go.Movetime(350))).through(write)
  def readResponses = read.pipe(collectResponses)
  def readUntilReady = readResponses.find(_ == ReadyOk)
  def readUntilBestmove = readResponses |> collectFirst { case bm: Bestmove => bm }

  def playGame(hist: Seq[GameState]): Process[Task, Seq[GameState]] = {
    toRawCommands(Position(hist)).through(write)
    .append(writeGo)
    .append(Process(hist).zip(readUntilBestmove).pipe(appendMove))
    .collect { case x: SimpleHistory => x }
    .observe(stdOutLastBoard)
    .flatMap(playGame)
  }

  (newGameCommands.through(write)
    .append(readUntilReady)
    .append(playGame(Vector(GameState.init))))
    .onComplete(toRawCommands(Quit).through(write))
    .run.run

  proc.destroy

}
 