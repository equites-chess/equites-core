package eu.timepit.equites
package cli

import proto._

import scalaz.stream._
import scalaz.concurrent.Task
import UciProcess._
import util.ScalazProcess._

import ProgramProcesses._

object UciExample extends App {

  type SimpleHistory = Vector[GameState]

  val (proc, write, read) = system("gnuchess", "-u")

  val writeStart = newGameCommands.through(write)
  def writeGo = toRawCommands(Uci.Go(Uci.Go.Movetime(350))).through(write)

  def readResponses = read.pipe(collectResponses)
  def readUntilReady = readResponses.find(_ == Uci.ReadyOk)
  def readUntilBestmove = readResponses |> collectFirst { case Uci.Bestmove(move, ponder) => move }

  def appendMove2(hist: SimpleHistory): Process1[util.CoordinateMove, SimpleHistory] =
    Process.await1[util.CoordinateMove].flatMap {
    case move =>
      val last = hist.last
      println(text.FigurineTextBoard.mkLabeled(last.board))

      val state = last.updated(move)
      state.map(s => Process(hist :+ s)).getOrElse(Process.halt)
  }

  val quit = toRawCommands(Uci.Quit).through(write)

  def playGame(hist: SimpleHistory): Process[Task, SimpleHistory] = {
    toRawCommands(Uci.Position(hist)).through(write)
    .append(writeGo)
    .append(readUntilBestmove.pipe(appendMove2(hist)))
    .collect { case x: SimpleHistory => x }
    .flatMap(x => playGame(x))
  }

  writeStart
    .append(readUntilReady)
    .append(playGame(Vector(GameState.init)))
    .append(quit).runLog.run.last

  proc.destroy
}
