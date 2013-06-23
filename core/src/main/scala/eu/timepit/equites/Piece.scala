// Equites, a Scala chess playground
// Copyright © 2011, 2013 Frank S. Thomas <frank@timepit.eu>
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

import PartialFunction.condOpt

sealed trait Piece {
  def color: Color

  def isFriendOf(other: Piece): Boolean = color == other.color
  def isOpponentOf(other: Piece): Boolean = color != other.color

  def maybeKing:   Option[King]   = condOpt(this) { case k: King   => k }
  def maybeQueen:  Option[Queen]  = condOpt(this) { case q: Queen  => q }
  def maybeRook:   Option[Rook]   = condOpt(this) { case r: Rook   => r }
  def maybeBishop: Option[Bishop] = condOpt(this) { case b: Bishop => b }
  def maybeKnight: Option[Knight] = condOpt(this) { case n: Knight => n }
  def maybePawn:   Option[Pawn]   = condOpt(this) { case p: Pawn   => p }

  def isKing:   Boolean = maybeKing.isDefined
  def isQueen:  Boolean = maybeQueen.isDefined
  def isRook:   Boolean = maybeRook.isDefined
  def isBishop: Boolean = maybeBishop.isDefined
  def isKnight: Boolean = maybeKnight.isDefined
  def isPawn:   Boolean = maybePawn.isDefined
}

sealed trait CastlingPiece extends Piece
sealed trait PromotedPiece extends Piece

case class King  (color: Color) extends CastlingPiece
case class Queen (color: Color) extends PromotedPiece
case class Rook  (color: Color) extends CastlingPiece with PromotedPiece
case class Bishop(color: Color) extends PromotedPiece
case class Knight(color: Color) extends PromotedPiece
case class Pawn  (color: Color) extends Piece
