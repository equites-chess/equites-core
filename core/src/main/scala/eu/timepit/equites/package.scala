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

package eu.timepit

package object equites {
  type AnyPiece = Piece[Color, PieceType]

  type CastlingPiece = Piece[Color, CastlingPieceType]
  type PromotedPiece = Piece[Color, PromotedPieceType]

  type AnyKing = Piece[Color, King.type]
  type AnyQueen = Piece[Color, Queen.type]
  type AnyRook = Piece[Color, Rook.type]
  type AnyBishop = Piece[Color, Bishop.type]
  type AnyKnight = Piece[Color, Knight.type]
  type AnyPawn = Piece[Color, Pawn.type]

  type WhitePiece[T <: PieceType] = Piece[White.type, T]
  type BlackPiece[T <: PieceType] = Piece[Black.type, T]

  type WhiteKing = WhitePiece[King.type]
  type WhiteQueen = WhitePiece[Queen.type]
  type WhiteRook = WhitePiece[Rook.type]
  type WhiteBishop = WhitePiece[Bishop.type]
  type WhiteKnight = WhitePiece[Knight.type]
  type WhitePawn = WhitePiece[Pawn.type]

  type BlackKing = BlackPiece[King.type]
  type BlackQueen = BlackPiece[Queen.type]
  type BlackRook = BlackPiece[Rook.type]
  type BlackBishop = BlackPiece[Bishop.type]
  type BlackKnight = BlackPiece[Knight.type]
  type BlackPawn = BlackPiece[Pawn.type]
}
