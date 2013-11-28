package eu.timepit.equites
package proto

import scalaz.stream._

import Uci._
import UciParsers._

object UciProcesses {
  def filterResponses: Process1[String, Response] =
    process1
    .lift(parseAll(response, _: String))
    .collect { case Success(result, _) => result }
  
  type SimpleHistory = Vector[GameState]

  def appendMove: Process1[(SimpleHistory, Bestmove), SimpleHistory] =
    process1.id[(SimpleHistory, Bestmove)].flatMap {
      case (history, bestmove) =>
        val state = history.last.updated(bestmove.move)
        state.map(s => Process(history :+ s)).getOrElse(Process.halt)
      }    
}
