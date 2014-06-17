// Equites, a Scala chess playground
// Copyright © 2014 Frank S. Thomas <frank@timepit.eu>
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
package util

object Pgn {
  case class Comment(text: String)

  case class Tag(name: String, value: String)

  ///

  sealed trait MoveElement

  case class MoveNumberIndicator(moveNumber: Int, color: Color) extends MoveElement

  case class SanMove(move: String) extends MoveElement

  case class AnnotationGlyph(glyph: Int) extends MoveElement

  ///

  sealed trait SeqElem

  case class SeqMoveElement(move: MoveElement) extends SeqElem

  case class SeqComment(comment: Comment) extends SeqElem

  case class RecursiveVariation(variation: List[SeqElem]) extends SeqElem

  ///

  case class MoveTextSection(moveText: List[SeqElem], result: GameResult)

  case class GameRecord(header: List[Tag], moveText: MoveTextSection)
}
