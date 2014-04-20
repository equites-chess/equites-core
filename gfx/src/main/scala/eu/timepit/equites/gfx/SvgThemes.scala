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

trait SvgTheme {
  def pieceElem(piece: AnyPiece): Elem
  def tileElem(color: Color): Elem
}

class SvgBoard(theme: SvgTheme) {
  def mkUnlabeled(board: Board): Elem = ???
}

object SvgThemes extends App {

  def pieceIds: Map[String, AnyPiece] =
    Piece.all.map(piece => (util.PieceUtil.showTextualId(piece), piece)).toMap

  def colorIds: Map[String, Color] =
    Color.all.map(color => (color.toString + "Tile", color)).toMap

  def extractElems[A](root: Elem, ids: Map[String, A]): Map[A, Elem] = {
    val mapping = for {
      child <- root.child.collect { case e: Elem => e }
      idText <- child.attribute("id").map(_.text)
      mappedA <- ids.get(idText)
    } yield mappedA -> child
    mapping.toMap
  }

  val dejavuTheme = XML.load(getClass.getResourceAsStream("/themes/DejaVuSans.svg"))
  val freeTheme = XML.load(getClass.getResourceAsStream("/themes/FreeSerif.svg"))
  val wikipediaTheme = XML.load(getClass.getResourceAsStream("/themes/Wikipedia.svg"))

  val pieces = extractElems(wikipediaTheme, pieceIds)
  val tiles = extractElems(wikipediaTheme, colorIds)

  println(tiles.get(White).get.attribute("x"))
  println(tiles.get(White).get.attribute("y"))

  def squareToUse(sq: Square): String = {
    val idT = util.SquareUtil.showAlgebraic(sq)
    val x = <use id="idT" xlink:href="#WhiteTile"/>
    sq.isDark
    ""
  }
  //Rules.allSquaresSeq.map
  // TODO make empty board
}
