package eu.timepit.equites
package cli

import proto._

import scalaz.stream._
import scalaz.concurrent.Task

object UciExample extends App {
  /*
  def linesR(in: java.io.InputStream): Process[Task,String] =
    io.resource(Task.delay(scala.io.Source.fromInputStream(in)))(
             x => Task.delay(())) { src =>
      lazy val lines = src.getLines // A stateful iterator
      Task.delay { if (lines.hasNext) lines.next else throw Process.End }
  }*/

  
  type SimpleHistory = Vector[GameState]
  def toByteArray[A](a: A): Array[Byte] = util.toUtf8(a.toString + "\n")

  val proc = (new java.lang.ProcessBuilder("gnuchess", "-u")).start

  val start: Process[Task, Uci.Command] =
    Process(Uci.Uci, Uci.UciNewGame, Uci.IsReady)

  var history: SimpleHistory = Vector(GameState.init)

  // Sink that writes uci commands to the engine
  def write: Sink[Task, Uci.Command] =
    io.chunkW(proc.getOutputStream)
    .map(f => {
      (i: Uci.Command) => {
        println("W: " + i)
        f(toByteArray(i)).onFinish(_ => Task(proc.getOutputStream.flush()))
      }
    })

  val in = io.chunkR(proc.getInputStream)
    
  // process that reads responses from the engine
  val read: Process[Task, Uci.Response] =
    Process(4096)
    .toSource
    //.through(in)
    .through(io.chunkR(proc.getInputStream()))
    .when(Process.eval(Task.delay { Thread.sleep(100); println("Checking input stream " + proc.getInputStream().available()); proc.getInputStream.available > 0 }))
    .map(new String(_))
    .map(_.split("\n"))
    .flatMap(Process.emitAll(_))
    .map(x => {println("I: "+ x); UciParsers.parseAll(UciParsers.response, x) } )
    .collect { case UciParsers.Success(result, _) => println("R: " + result); result }
    //.repeat

  /*def read2: Process[Task, Uci.Response] =
    linesR(proc.getInputStream)
    .map(x => UciParsers.parseAll(UciParsers.response, x))
    .collect { case UciParsers.Success(result, _) => println("R: " + result); result }
  */
  
  // process that takes a history and outputs Uci.Position and Go
  val toGo: Process1[SimpleHistory, Uci.Request] = for {
    hist <- process1.id[SimpleHistory]
    cmds <- Process.emitAll(Seq(Uci.Position(hist), Uci.Go(Uci.Go.Movetime(100)))) 
  } yield cmds

  val toGo2: Process1[SimpleHistory, Uci.Request] = for {
    hist <- Process.await1[SimpleHistory]
    cmds <- Process.emitAll(Seq(Uci.Position(hist), Uci.Go(Uci.Go.Movetime(100))))
  } yield cmds

  val appendMove: Process1[(SimpleHistory, Uci.Bestmove), SimpleHistory] =
    process1.id[(SimpleHistory, Uci.Bestmove)].flatMap {    
      case (history, bestmove) =>
        val state = history.last.updated(bestmove.move)
        state.map(s => Process(history :+ s)).getOrElse(Process.halt)
      }
/*
  
  loop: (History, Bestmove) -> new History -> (write new History and transfer it)
        -> transfer History read responses
  
 
 */


 
  
 
  val p: Process[Task, Uci.Command] =
    start.observe(write)
    .append(read)
    .append( Process(history).toSource.pipe(toGo).observe(write))
    .append( read)

    println(p.runLog.run)
  
  


  
  //val in = start.append(read)

  //println(in.observe(write).runLog.run)

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