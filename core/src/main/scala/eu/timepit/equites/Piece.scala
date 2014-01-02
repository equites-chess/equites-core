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

import scalaz._
import Scalaz._

object Piece {
  def all: List[AnyPiece] = mkAllPieces(PieceType.all)
  def allCastling: List[CastlingPiece] = mkAllPieces(PieceType.allCastling)
  def allPromoted: List[PromotedPiece] = mkAllPieces(PieceType.allPromoted)

  private def mkAllPieces[T <: PieceType](pieceTypes: List[T]): List[Piece[Color, T]] =
    ^(Color.all, pieceTypes)(Piece.apply)
}

case class Piece[+C <: Color, +T <: PieceType](color: C, pieceType: T) {
  type ColorT = C
  type PieceTypeT = T

  def isFriendOf(other: AnyPiece): Boolean = color == other.color
  def isOpponentOf(other: AnyPiece): Boolean = color != other.color

  // format: OFF
  def isKing   = is(King)
  def isQueen  = is(Queen)
  def isRook   = is(Rook)
  def isBishop = is(Bishop)
  def isKnight = is(Knight)
  def isPawn   = is(Pawn)

  def maybeKing   = maybe(King)
  def maybeQueen  = maybe(Queen)
  def maybeRook   = maybe(Rook)
  def maybeBishop = maybe(Bishop)
  def maybeKnight = maybe(Knight)
  def maybePawn   = maybe(Pawn)
  // format: ON

  private[this] def is(pType: PieceType): Boolean = pieceType == pType

  private[this] def maybe[T1 <: PieceType](pType: T1): Option[Piece[C, T1]] =
    (pieceType == pType).option(Piece(color, pType))
}

trait PieceTypeAliases {
  // format: OFF
  type AnyPiece      = Piece[Color, PieceType]
  type CastlingPiece = Piece[Color, CastlingPieceType]
  type PromotedPiece = Piece[Color, PromotedPieceType]

  type AnyKing   = Piece[Color, King.type]
  type AnyQueen  = Piece[Color, Queen.type]
  type AnyRook   = Piece[Color, Rook.type]
  type AnyBishop = Piece[Color, Bishop.type]
  type AnyKnight = Piece[Color, Knight.type]
  type AnyPawn   = Piece[Color, Pawn.type]

  type WhiteKing   = WhitePiece[King.type]
  type WhiteQueen  = WhitePiece[Queen.type]
  type WhiteRook   = WhitePiece[Rook.type]
  type WhiteBishop = WhitePiece[Bishop.type]
  type WhiteKnight = WhitePiece[Knight.type]
  type WhitePawn   = WhitePiece[Pawn.type]

  type BlackKing   = BlackPiece[King.type]
  type BlackQueen  = BlackPiece[Queen.type]
  type BlackRook   = BlackPiece[Rook.type]
  type BlackBishop = BlackPiece[Bishop.type]
  type BlackKnight = BlackPiece[Knight.type]
  type BlackPawn   = BlackPiece[Pawn.type]
  // format: ON

  private type WhitePiece[T <: PieceType] = Piece[White.type, T]
  private type BlackPiece[T <: PieceType] = Piece[Black.type, T]
}
