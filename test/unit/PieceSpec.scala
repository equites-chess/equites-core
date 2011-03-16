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

import org.specs2.mutable._

class PieceSpec extends Specification {
  "Piece" should {
    "be constructable from algebraic notation" in {
      Piece("K", White) must_== new King(White)
      Piece("Q", White) must_== new Queen(White)
      Piece("R", White) must_== new Rook(White)
      Piece("B", White) must_== new Bishop(White)
      Piece("N", White) must_== new Knight(White)
      Piece("P", White) must_== new Pawn(White)

      Piece("K", Black) must_== new King(Black)
      Piece("Q", Black) must_== new Queen(Black)
      Piece("R", Black) must_== new Rook(Black)
      Piece("B", Black) must_== new Bishop(Black)
      Piece("N", Black) must_== new Knight(Black)
      Piece("P", Black) must_== new Pawn(Black)

      Piece("k", Black) must_== new King(Black)
      Piece("q", Black) must_== new Queen(Black)
      Piece("r", Black) must_== new Rook(Black)
      Piece("b", Black) must_== new Bishop(Black)
      Piece("n", Black) must_== new Knight(Black)
      Piece("p", Black) must_== new Pawn(Black)
    }

    "fail on invalid algebraic notation" in {
      Piece("",   White) must throwAn[IllegalArgumentException]
      Piece("A",  White) must throwAn[IllegalArgumentException]
      Piece("QQ", White) must throwAn[IllegalArgumentException]
    }
  }
}
