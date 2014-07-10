// Equites, a Scala chess playground
// Copyright © 2011, 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import util.DrawUtil._
import util.PieceUtil._

object ActionUtil {
  def showAlgebraicCastling(side: Side): String =
    side.fold("0-0", "0-0-0")

  def showCoordinate(action: Action): String =
    CoordinateAction(action).toAlgebraic

  def showFenCastlingLetter(castling: Castling): String = {
    val letter = castling.side.fold("K", "Q")
    castling.color.fold(letter, letter.toLowerCase)
  }

  def showLongAlgebraic(action: Action): String =
    showLongAlgebraicImpl(action, p => PieceUtil.showAlgebraic(p.pieceType))

  def showLongFigurine(action: Action): String =
    showLongAlgebraicImpl(action, showFigurine)

  def showLongAlgebraicImpl(action: Action, showPiece: AnyPiece => String): String =
    action match {
      case p: PromotionLike => showPiece(p.piece) + showNakedLongAlgebraic(action) + showPiece(p.promotedTo)
      case m: MoveLike      => showPiece(m.piece) + showNakedLongAlgebraic(action)
      case _                => showNakedLongAlgebraic(action)
    }

  def showNakedLongAlgebraic(action: Action): String =
    action match {
      case e: EnPassant   => algebraicSquares(e.draw).mkString("x") + "e.p."
      case c: CaptureLike => algebraicSquares(c.draw).mkString("x")
      case m: MoveLike    => algebraicSquares(m.draw).mkString("-")
      case c: Castling    => showAlgebraicCastling(c.side)
    }

  def showNumeric(action: Action): String =
    numericSquares(action.draw).mkString +
      ActionOps.promotedPiece(action).map(_.pieceType).fold("")(PieceUtil.showNumeric)
}
