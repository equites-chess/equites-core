package eu.timepit.equites
package proto

import org.specs2.mutable._
import scalaz.concurrent.Task
import scalaz.stream._

import util.SquareAbbr._
import UciProcesses._

class UciProcessesSpec extends Specification {
  "???" >> {
    "???" in {
      val x: Process[Task, String] = Process("uciok\n", "foo bar\r\n", "id author John")
      x.pipe(filterResponses).runLog.run.toList must_== List(Uci.UciOk, Uci.Id("author", "John"))
    }

    "???" in {
      val cm = util.CoordinateMove(e2, e4)
      val history = Vector(GameState.init)
      //val x: Process[Task, (SimpleHistory, Uci.Bestmove)] = Process((history, Uci.Bestmove(util.CoordinateMove(e2, e4))))
      val x = Process((history, Uci.Bestmove(util.CoordinateMove(e2, e4))))
      x.toSource.pipe(appendMove).runLog.run.toList must_== List(history :+ history.last.updated(cm).get)
    }
    
    "???" in {
      val cm = util.CoordinateMove(e2, e4)
      val cm2 = util.CoordinateMove(Square(-1,-1), Square(-1,-1))
      val history = Vector(GameState.init)
      //val x: Process[Task, (SimpleHistory, Uci.Bestmove)] = Process((history, Uci.Bestmove(util.CoordinateMove(e2, e4))))
      val x = Process((history, Uci.Bestmove(cm2)))
      x.toSource.pipe(appendMove).runLog.run.toList must_!= List(history)
    }
  }
}
