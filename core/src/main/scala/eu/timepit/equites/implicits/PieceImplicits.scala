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
package implicits

object PieceImplicits {
  implicit final class RichPiece(val self: AnyPiece) extends AnyVal {
    def toAlgebraic: String = self.pieceType match {
      case King   => "K"
      case Queen  => "Q"
      case Rook   => "R"
      case Bishop => "B"
      case Knight => "N"
      case Pawn   => ""
    }

    def toUpperCaseLetter: String = self.pieceType match {
      case Pawn => "P"
      case _    => toAlgebraic
    }

    def toLowerCaseLetter: String = toUpperCaseLetter.toLowerCase

    def toLetter: String = self.color match {
      case White => toUpperCaseLetter
      case Black => toLowerCaseLetter
    }

    def toFigurine: String = self match {
      case Piece(White, King)   => "♔"
      case Piece(White, Queen)  => "♕"
      case Piece(White, Rook)   => "♖"
      case Piece(White, Bishop) => "♗"
      case Piece(White, Knight) => "♘"
      case Piece(White, Pawn)   => "♙"
      case Piece(Black, King)   => "♚"
      case Piece(Black, Queen)  => "♛"
      case Piece(Black, Rook)   => "♜"
      case Piece(Black, Bishop) => "♝"
      case Piece(Black, Knight) => "♞"
      case Piece(Black, Pawn)   => "♟"
    }

    def toWikiLetters: String = {
      def colorLetter: String = self.color match {
        case White => "l"
        case Black => "d"
      }
      toLowerCaseLetter + colorLetter
    }

    def toNumeric: String = self.pieceType match {
      case Queen  => "1"
      case Rook   => "2"
      case Bishop => "3"
      case Knight => "4"
      case _      => ""
    }

    def centipawns: Int = self.pieceType match {
      case King   => 100000000
      case Queen  => 900
      case Rook   => 500
      case Bishop => 300
      case Knight => 300
      case Pawn   => 100
    }

    def toTextualId: String = self.color.toString + self.pieceType.toString
  }
}
