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
  implicit final class RichPiece(val self: Piece) extends AnyVal {
    def toAlgebraic: String = self match {
      case King(_)   => "K"
      case Queen(_)  => "Q"
      case Rook(_)   => "R"
      case Bishop(_) => "B"
      case Knight(_) => "N"
      case Pawn(_)   => ""
    }

    def toUpperCaseLetter: String = self match {
      case Pawn(_) => "P"
      case _       => toAlgebraic
    }

    def toLowerCaseLetter: String = toUpperCaseLetter.toLowerCase

    def toLetter: String = self.color match {
      case White => toUpperCaseLetter
      case Black => toLowerCaseLetter
    }

    def toFigurine: String = self match {
      case King(White)   => "♔"
      case Queen(White)  => "♕"
      case Rook(White)   => "♖"
      case Bishop(White) => "♗"
      case Knight(White) => "♘"
      case Pawn(White)   => "♙"
      case King(Black)   => "♚"
      case Queen(Black)  => "♛"
      case Rook(Black)   => "♜"
      case Bishop(Black) => "♝"
      case Knight(Black) => "♞"
      case Pawn(Black)   => "♟"
    }

    def toWikiLetters: String = toLowerCaseLetter + (self.color match {
      case White => "l"
      case Black => "d"
    })

    def toNumeric: String = self match {
      case Queen(_)  => "1"
      case Rook(_)   => "2"
      case Bishop(_) => "3"
      case Knight(_) => "4"
      case _         => ""
    }

    def centipawns: Int = self match {
      case King(_)   => 100000000
      case Queen(_)  => 900
      case Rook(_)   => 500
      case Bishop(_) => 300
      case Knight(_) => 300
      case Pawn(_)   => 100
    }

    def toThemeId: String =
      self.color.toString + self.getClass.getSimpleName
  }
}
