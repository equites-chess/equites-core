package eu.timepit.equites
package cli

import proto._

import scalaz.stream._
import scalaz.concurrent.Task
import UciProcesses._

import ProgramProcesses._

object UciExample extends App {

  val tb = text.FigurineTextBoard

  type SimpleHistory = collection.mutable.ListBuffer[GameState]
  var history: SimpleHistory = collection.mutable.ListBuffer(GameState.init)

  val (proc, write, read) = system("gnuchess", "-u")

  val start = toRawCommands(Uci.Uci, Uci.UciNewGame, Uci.IsReady)
  val writeStart = start.through(write)
  def writeHistory = { Process.await(Task.delay(Uci.Position(history)))(x => Process(x)).map(util.toUtf8Ln).through(write) }
  def writeGo = toRawCommands(Uci.Go(Uci.Go.Movetime(200))).through(write)

  def readResponses = read.pipe(collectResponses)
  def readUntilReady = readResponses.dropWhile(_ != Uci.ReadyOk).take(1)
  def readUntilBestmove = readResponses.collect { case Uci.Bestmove(move, ponder) => move }.take(1)
  def appendMove = Process.await1[util.CoordinateMove].flatMap {
    case move =>
      val last = history.last
      println(tb.mkLabeled(last.board))
      println(move)

      val state = last.updated(move)
      state.map(s => { history += s; Process(history) }).getOrElse(Process.halt)
  }

  val quit = toRawCommands(Uci.Quit).through(write)

  def move = writeHistory
    .append(writeGo)
    .append(readUntilBestmove.pipe(appendMove))

  writeStart
    .append(readUntilReady)
    .append(move.repeat)
    .append(quit).runLog.run.last

  proc.destroy

}

/*
object UciExample extends App {
  val proc = (new java.lang.ProcessBuilder("gnuchess", "-u")).start
  val procIn = proc.getOutputStream
  val procOut = scala.io.Source.fromInputStream(proc.getInputStream)

  def write(cmd: Uci.Command): Unit = {
    procIn.write(util.toUtf8(cmd))
    procIn.write("\n".getBytes)
    procIn.flush
  }

  val tb = new text.TextBoard with text.FigurineRepr
  var history = Vector(GameState.init)

  def requestNextMove(): Unit = {
    write(Uci.Position(history))
    write(Uci.Go(Uci.Go.Movetime(100)))
  }

  def reactOn(cmd: Uci.Response) = cmd match {
    case Uci.ReadyOk => requestNextMove()
    case Uci.Bestmove(cm, _) => {
      val last = history.last
      println(tb.mkLabeled(last.board))

      val newState = last.updated(cm)
      if (newState.nonEmpty) {
        history = history :+ newState.get
        requestNextMove()
      } else {
        write(Uci.Quit)
      }
    }
    case _ => ()
  }

  write(Uci.Uci)
  write(Uci.UciNewGame)
  write(Uci.IsReady)

  procOut.getLines.foreach { line =>
    UciParsers.parseAll(UciParsers.response, line) match {
      case UciParsers.Success(result, _) => reactOn(result)
      case _ => ()
    }
  }

  proc.waitFor
  procIn.close
  procOut.close
}
*/