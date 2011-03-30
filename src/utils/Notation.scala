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

package equites
package utils

object Notation {
  def toAlgebraic(piece: Piece): String = piece.pieceType match {
    case King   => "K"
    case Queen  => "Q"
    case Rook   => "R"
    case Bishop => "B"
    case Knight => "N"
    case Pawn   => ""
  }

  def toLetter(piece: Piece): String = {
    val letter = if (piece.pieceType == Pawn) "P" else toAlgebraic(piece)
    if (piece.color == White) letter else letter.toLowerCase
  }

  def toSymbol(piece: Piece): String = {
    val symbols = piece.pieceType match {
      case King   => ("\u2654", "\u265A") // ♔ ♚
      case Queen  => ("\u2655", "\u265B") // ♕ ♛
      case Rook   => ("\u2656", "\u265C") // ♖ ♜
      case Bishop => ("\u2657", "\u265D") // ♗ ♝
      case Knight => ("\u2658", "\u265E") // ♘ ♞
      case Pawn   => ("\u2659", "\u265F") // ♙ ♟
    }
    if (piece.color == White) symbols._1 else symbols._2
  }
}
