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
package util

import PieceAbbr.Algebraic._
import PieceAbbr.Figurine._

object PieceUtil {
  def centipawns(pieceType: PieceType): Int =
    pieceType match {
      case King   => 100000000
      case Queen  => 900
      case Rook   => 500
      case Bishop => 300
      case Knight => 300
      case Pawn   => 100
    }

  def readFigurine(char: Char): Option[AnyPiece] =
    char match {
      case '♔' => Some(♔)
      case '♕' => Some(♕)
      case '♖' => Some(♖)
      case '♗' => Some(♗)
      case '♘' => Some(♘)
      case '♙' => Some(♙)
      case '♚' => Some(♚)
      case '♛' => Some(♛)
      case '♜' => Some(♜)
      case '♝' => Some(♝)
      case '♞' => Some(♞)
      case '♟' => Some(♟)
      case _   => None
    }

  def readLetter(char: Char): Option[AnyPiece] =
    char match {
      case 'K' => Some(K)
      case 'Q' => Some(Q)
      case 'R' => Some(R)
      case 'B' => Some(B)
      case 'N' => Some(N)
      case 'P' => Some(P)
      case 'k' => Some(k)
      case 'q' => Some(q)
      case 'r' => Some(r)
      case 'b' => Some(b)
      case 'n' => Some(n)
      case 'p' => Some(p)
      case _   => None
    }

  def showAlgebraic(pieceType: PieceType): String =
    pieceType match {
      case King   => "K"
      case Queen  => "Q"
      case Rook   => "R"
      case Bishop => "B"
      case Knight => "N"
      case Pawn   => ""
    }

  def showFigurine(piece: AnyPiece): String =
    piece match {
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

  def showLetter(piece: AnyPiece): String =
    piece.color.fold(showUpperCaseLetter _, showLowerCaseLetter _)(piece.pieceType)

  def showLowerCaseLetter(pieceType: PieceType): String =
    showUpperCaseLetter(pieceType).toLowerCase

  def showNumeric(pieceType: PieceType): String =
    pieceType match {
      case Queen  => "1"
      case Rook   => "2"
      case Bishop => "3"
      case Knight => "4"
      case _      => ""
    }

  def showTextualId(piece: AnyPiece): String =
    piece.color.toString + piece.pieceType.toString

  def showUpperCaseLetter(pieceType: PieceType): String =
    pieceType match {
      case Pawn => "P"
      case _    => showAlgebraic(pieceType)
    }

  def showWikiLetters(piece: AnyPiece): String =
    showLowerCaseLetter(piece.pieceType) + piece.color.fold("l", "d")
}
