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
package implicits

object RichPieceImplicit {
  implicit def   wrapPiece(piece: Piece) = new RichPiece(piece)
  implicit def unwrapPiece(piece: RichPiece) = piece.self
}

final class RichPiece(val self: Piece) extends Proxy {
  def toAlgebraic: String = self.pieceType match {
    case King   => "K"
    case Queen  => "Q"
    case Rook   => "R"
    case Bishop => "B"
    case Knight => "N"
    case Pawn   => ""
  }

  def toLetter: String = {
    val letter = if (self.pieceType == Pawn) "P" else toAlgebraic
    if (self.color == White) letter else letter.toLowerCase
  }

  def toFigurine: String = (self.color, self.pieceType) match {
    case (White, King)   => "\u2654" // ♔
    case (White, Queen)  => "\u2655" // ♕
    case (White, Rook)   => "\u2656" // ♖
    case (White, Bishop) => "\u2657" // ♗
    case (White, Knight) => "\u2658" // ♘
    case (White, Pawn)   => "\u2659" // ♙
    case (Black, King)   => "\u265A" // ♚
    case (Black, Queen)  => "\u265B" // ♛
    case (Black, Rook)   => "\u265C" // ♜
    case (Black, Bishop) => "\u265D" // ♝
    case (Black, Knight) => "\u265E" // ♞
    case (Black, Pawn)   => "\u265F" // ♟
  }

  def toWikiLetters: String = {
    val color = if (self.color == White) "l" else "d"
    toLetter.toLowerCase + color
  }

  def toNumeric: String = self.pieceType match {
    case Queen  => "1"
    case Rook   => "2"
    case Bishop => "3"
    case Knight => "4"
    case _ => ""
  }

  def centipawns: Int = self.pieceType match {
    case King   => 100000000
    case Queen  => 900
    case Rook   => 500
    case Bishop => 300
    case Knight => 300
    case Pawn   => 100
  }
}
