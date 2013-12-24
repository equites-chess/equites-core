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
package gfx

import scala.xml._

import implicits.PieceImplicits._

object SvgThemes extends App {
  type PieceIds = Map[String, AnyPiece]

  def pieceImageIds: PieceIds =
    Piece.all.map(piece => (piece.toTextualId, piece)).toMap

  def pieceTextIds: PieceIds =
    pieceImageIds.map { case (id, piece) => (id + "Text", piece) }

  def extractPieceElems(root: Elem, label: String, ids: PieceIds): Map[AnyPiece, Elem] = {
    val childElems = (root \ label).theSeq.collect { case e: Elem => e }
    childElems.flatMap { child =>
      val maybeId = child.attribute("id").map(_.text)
      maybeId.flatMap(ids.get).map(_ -> child)
    }.toMap
  }




  XML.load(getClass.getResourceAsStream("/themes/DejaVuSans.svg"))
  val freeTheme = XML.load(getClass.getResourceAsStream("/themes/FreeSerif.svg"))
  val wikipediaTheme = XML.load(getClass.getResourceAsStream("/themes/Wikipedia.svg"))

  println(extractPieceElems(freeTheme, "text", pieceTextIds).get(Piece(White, King)))

}
