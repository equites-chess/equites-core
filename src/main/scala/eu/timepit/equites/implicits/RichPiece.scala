// Equites, a simple chess interface
// Copyright © 2011, 2013 Frank S. Thomas <f.thomas@gmx.de>
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

object RichPieceImplicit {
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
      case _ => toAlgebraic
    }

    def toLowerCaseLetter: String = toUpperCaseLetter.toLowerCase

    def toLetter: String = self.color match {
      case White => toUpperCaseLetter
      case Black => toLowerCaseLetter
    }

    def toFigurine: String = self match {
      case King(White)   => "\u2654" // ♔
      case Queen(White)  => "\u2655" // ♕
      case Rook(White)   => "\u2656" // ♖
      case Bishop(White) => "\u2657" // ♗
      case Knight(White) => "\u2658" // ♘
      case Pawn(White)   => "\u2659" // ♙
      case King(Black)   => "\u265A" // ♚
      case Queen(Black)  => "\u265B" // ♛
      case Rook(Black)   => "\u265C" // ♜
      case Bishop(Black) => "\u265D" // ♝
      case Knight(Black) => "\u265E" // ♞
      case Pawn(Black)   => "\u265F" // ♟
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
  }
}
