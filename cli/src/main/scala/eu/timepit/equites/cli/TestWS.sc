package eu.timepit.equites
package cli

import scalaz.stream._
import ProgramProcesses._

import proto._
import UciProcesses._

object TestWS {
  val p1 = Process(1)                             //> p1  : scalaz.stream.Process[Nothing,Int] = Emit(WrappedArray(1),Halt(scalaz.
                                                  //| stream.Process$End$))
  val p2: Process1[Int, Int] = Process.await1[Int].map(_ + 1)
                                                  //> p2  : scalaz.stream.Process1[Int,Int] = Await(Left,<function1>,Halt(scalaz.s
                                                  //| tream.Process$End$),Halt(scalaz.stream.Process$End$))
  
  val x = Process.unfold((0, 1))(x => Some( (x._1, x._2), (x._2, x._1 + x._2))).map(_._1)
                                                  //> x  : scalaz.stream.Process[scalaz.concurrent.Task,Int] = Await(scalaz.concur
                                                  //| rent.Task@1046128d,<function1>,Halt(scalaz.stream.Process$End$),Halt(scalaz.
                                                  //| stream.Process$End$))
  x.take(40).runLog.run                           //> res0: scala.collection.immutable.IndexedSeq[Int] = Vector(0, 1, 1, 2, 3, 5, 
                                                  //| 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 1094
                                                  //| 6, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 13462
                                                  //| 69, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169, 632459
                                                  //| 86)
  


/*
  def toByteArray[A](a: A): Array[Byte] = util.toUtf8(a.toString + "\n")

  val tb = new text.TextBoard with text.FigurineRepr

  type SimpleHistory = Vector[GameState]
  var history: SimpleHistory = Vector(GameState.init)

  val (proc, write, read) = system("gnuchess", "-u")

  val start = Process(Uci.Uci, Uci.UciNewGame, Uci.IsReady).map(toByteArray).toSource
  val writeStart = start.through(write)
  def writeHistory = Process(Uci.Position(history)).map(toByteArray).toSource.through(write)
  def writeGo = Process(Uci.Go(Uci.Go.Movetime(100))).map(toByteArray).toSource.through(write)

  def readResponses = read.pipe(collectResponses)
  def readUntilReady = readResponses.dropWhile(_ != Uci.ReadyOk).take(1)
  def readUntilBestmove = readResponses.collect { case Uci.Bestmove(move, ponder) => move }.take(1)
  def appendMove = Process.await1[util.CoordinateMove].flatMap {
    case move =>
      val last = history.last
      println(tb.mkLabeled(last.board))

      val state = last.updated(move)
      state.map(s => { history = history :+ s; Process(history) }).getOrElse(Process.halt)
  }

  val quit = Process(Uci.Quit).map(toByteArray).toSource.through(write)

  val move = writeHistory
    .append(writeGo)
    .append(readUntilBestmove.pipe(appendMove))
    .repeat

  writeStart
    .append(readUntilReady)
    .append(move)
    .append(quit).runLog.run.last

  proc.destroy*/
}