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
package implicits

import PieceImplicits._
import SquareImplicits._

object ActionImplicits {
  implicit final class RichAction(val self: Action) extends AnyVal {
    def toLongAlgebraic: String = toLongAlgebraicImpl(_.toAlgebraic)
    def toLongFigurine: String = toLongAlgebraicImpl(_.toFigurine)

    private def toLongAlgebraicImpl(showPiece: AnyPiece => String): String =
      self match {
        case p: PromotionLike => showPiece(p.piece) + nakedLongAlgebraic + showPiece(p.promotedTo)
        case m: MoveLike      => showPiece(m.piece) + nakedLongAlgebraic
        case _                => nakedLongAlgebraic
      }

    private def nakedLongAlgebraic: String = self match {
      case e: EnPassant     => algebraicSquares(e).mkString("x") + "e.p."
      case c: CaptureLike   => algebraicSquares(c).mkString("x")
      case m: MoveLike      => algebraicSquares(m).mkString("-")
      case c: CastlingShort => "0-0"
      case c: CastlingLong  => "0-0-0"
    }

    def toNumeric: String = self match {
      case p: PromotionLike => numericSquares(p).mkString + p.promotedTo.toNumeric
      case m: MoveLike      => numericSquares(m).mkString
      case c: Castling      => c.kingMove.toNumeric
    }

    def toCoordinate: String = self match {
      case p: PromotionLike => algebraicSquares(p).mkString + p.promotedTo.toLowerCaseLetter
      case m: MoveLike      => algebraicSquares(m).mkString
      case c: Castling      => c.kingMove.toCoordinate
    }

    private def algebraicSquares(move: MoveLike): Seq[String] =
      move.squares.map(_.toAlgebraic)

    private def numericSquares(move: MoveLike): Seq[String] =
      move.squares.map(_.toNumeric)
  }
}
