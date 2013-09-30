package eu.timepit.equites
package proto

object UciExample extends App {
  val proc = new java.lang.ProcessBuilder("gnuchess", "-u").start
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
