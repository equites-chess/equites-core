package eu.timepit.equites
package proto

object UciExample extends App {
  val proc = new java.lang.ProcessBuilder("gnuchess", "-u").start
  val procIn = proc.getOutputStream
  val procOut = scala.io.Source.fromInputStream(proc.getInputStream)

  def write(cmd: Uci.Command): Unit = {
    val str = s"${cmd.toString}\n"
    procIn.write(str.getBytes("UTF-8"))
    procIn.flush
  }

  val tb = new text.TextBoard with text.FigurineRepr
  var history = Vector(GameState.init)

  def go(): Unit = {
    write(Uci.Position(history))
    write(Uci.Go(Uci.Go.Movetime(100)))
  }

  def reactOn(cmd: Uci.Response) = cmd match {
    case Uci.ReadyOk => go()
    case Uci.Bestmove(cm, _) => {
      val last = history.last
      val action = ActionOps.cmAsAction(last.board)(cm)

      println(tb.mkLabeled(last.board))

      if (action.isEmpty) {
        write(Uci.Quit)
      } else {
        history = history :+ last.updated(action.get)
        go()
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
