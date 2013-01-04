// Equites, a simple chess interface
// Copyright © 2011 Frank S. Thomas <f.thomas@gmx.de>
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
package implicits

import RichActionImplicit._
import RichSquareImplicit._
import RichPieceImplicit._
import utils.Notation

object RichActionImplicit {
  implicit def   wrapAction(action: Action) = new RichAction(action)
  implicit def unwrapAction(action: RichAction) = action.self
}

final class RichAction(val self: Action) extends Proxy {
/*  def toLongAlgebraic: String = toLongAlgebraic(piece => piece.toAlgebraic)
  def toLongFigurine:  String = toLongAlgebraic(piece => piece.toFigurine)

  private def toLongAlgebraic(stringOf: Piece => String): String = {
    (self match {
      case p: PromotionLike
        => stringOf(p.piece) + moveToLongAlgebraic + stringOf(p.newPiece)
      case m: MoveLike
        => stringOf(m.piece) + moveToLongAlgebraic
      case _ => moveToLongAlgebraic
    }) + flagsToAlgebraic
  }*/
/*
  private def moveToLongAlgebraic: String = self match {
    case e: EnPassant
      => e.from.toAlgebraic + "x" + e.to.toAlgebraic + "e.p."
    case c: CaptureLike
      => c.from.toAlgebraic + "x" + c.to.toAlgebraic
    case m: MoveLike
      => m.from.toAlgebraic + "-" + m.to.toAlgebraic
    case c: CastlingShort => "0-0"
    case c: CastlingLong  => "0-0-0"
  }*/
/*
  private def flagsToAlgebraic: String = {
    if (self.isChecking)    return "+"
    if (self.isCheckmating) return "#"
    if (self.isStalemating) return ""
    ""
  }
  */
/*
  def toNumeric: String = self match {
    case p: PromotionLike
      => p.from.toNumeric + p.to.toNumeric + p.newPiece.toNumeric
    case m: MoveLike
      => m.from.toNumeric + m.to.toNumeric
    case c: Castling
      => c.kingMove.toNumeric
  }*/
}
