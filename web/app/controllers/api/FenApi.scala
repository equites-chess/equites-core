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
package controllers
package api

import play.api.mvc.Action
import play.api.mvc.Results.Ok

object FenApi {
  def toTextRepr(placement: String, textBoard: text.TextBoard) = Action {
    val board = util.Notation.boardFromFen(placement)
    Ok(textBoard.mkLabeled(board))
  }

  def toFigurineBoard(placement: String) =
    toTextRepr(placement, text.FigurineTextBoard)

  def toLetterBoard(placement: String) =
    toTextRepr(placement, text.LetterTextBoard)

  def toWikiBoard(placement: String) =
    toTextRepr(placement, text.WikiTextBoard)
}
