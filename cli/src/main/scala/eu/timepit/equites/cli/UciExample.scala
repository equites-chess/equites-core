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
  val history: SimpleHistory = Vector(GameState.init)

  val (proc, write, read) = system("gnuchess", "-u")

  val start = toRawCommands(Uci.Uci, Uci.UciNewGame, Uci.IsReady)
  val writeStart = start.through(write)
  def writeHistory = { Process.await(Task.delay(Uci.Position(history)))(x => Process(x)).map(util.toUtf8BytesLf).through(write) }
  def writeGo = toRawCommands(Uci.Go(Uci.Go.Movetime(750))).through(write)

  def readResponses = read.pipe(collectResponses)
  def readUntilReady = readResponses.find(_ == Uci.ReadyOk)
  def readUntilBestmove = readResponses |> collectFirst { case Uci.Bestmove(move, ponder) => move }

  def appendMove2(hist: SimpleHistory): Process1[util.CoordinateMove, SimpleHistory] =
    Process.await1[util.CoordinateMove].flatMap {
    case move =>
      val last = hist.last
      println(text.FigurineTextBoard.mkLabeled(last.board))

      val state = last.updated(move)
      state.map(s => Process(hist ++ Seq(s))).getOrElse(Process.halt)
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
    .append(playGame(history))
    .append(quit).runLog.run.last

  proc.destroy
}
