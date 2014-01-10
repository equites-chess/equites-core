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
package proto

import scalaz.concurrent.Task
import scalaz.stream._

import Uci._
import UciParsers._
import util.CoordinateMove
import util.ScalazProcess._

object UciProcess {
  type HistoryTransformer[A] = Process1[(Seq[GameState], A), Seq[GameState]]

  def appendBestmove: HistoryTransformer[Bestmove] =
    Process.await1[(Seq[GameState], Bestmove)].map {
      case (history, bestmove) => (history, bestmove.move)
    } |> appendCoordinateMove

  def appendCoordinateMove: HistoryTransformer[CoordinateMove] =
    Process.await1[(Seq[GameState], CoordinateMove)].flatMap {
      case (history, move) => {
        val state = history.lastOption.flatMap(_.updated(move))
        state.map(s => Process(history :+ s)).getOrElse(Process.halt)
      }
    }

  def collectResponses: Process1[String, Response] =
    process1
      .lift((str: String) => parseAll(response, str))
      .collect { case Success(result, _) => result }

  def newGameCommands: Process[Task, String] =
    toRawCommands(Uci.Uci, UciNewGame, IsReady)

  /**
   * Returns a `Sink` that prints the last board of a given sequence of
   * `GameState`s to standard output.
   */
  def stdOutLastBoard: Sink[Task, Seq[GameState]] = {
    val tb = text.FigurineTextBoard
    io.stdOutLines.contramap {
      (history: Seq[GameState]) =>
        history.lastOption.fold("")(state => tb.mkLabeled(state.board))
    }
  }
}
