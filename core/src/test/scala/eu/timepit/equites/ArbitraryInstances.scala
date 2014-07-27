// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import org.scalacheck.{ Arbitrary, Gen }
import org.scalacheck.Arbitrary.arbitrary

object ArbitraryInstances {
  implicit val arbitraryBoard: Arbitrary[Board] =
    Arbitrary {
      val genSquarePiece = for {
        square <- arbitrary[Square]
        piece <- arbitrary[AnyPiece]
      } yield (square, piece)

      Gen.containerOf[List, (Square, AnyPiece)](genSquarePiece)
        .map(seq => Board(seq: _*))
    }

  implicit val arbitraryColor: Arbitrary[Color] =
    Arbitrary(Gen.oneOf(Color.all))

  implicit def arbitraryPlaced[A: Arbitrary]: Arbitrary[Placed[A]] =
    Arbitrary {
      for {
        elem <- arbitrary[A]
        square <- arbitrary[Square]
      } yield Placed(elem, square)
    }

  implicit val arbitraryPieceType: Arbitrary[PieceType] =
    Arbitrary(Gen.oneOf(PieceType.all))

  implicit val arbitraryPieceFn: Arbitrary[Color => AnyPiece] =
    Arbitrary {
      for {
        pieceType <- arbitrary[PieceType]
      } yield (c: Color) => Piece(c, pieceType)
    }

  implicit val arbitraryPiece: Arbitrary[AnyPiece] =
    Arbitrary(Gen.oneOf(Piece.all))

  implicit val arbitraryPromotedPiece: Arbitrary[PromotedPiece] =
    Arbitrary(Gen.oneOf(Piece.allPromoted))

  implicit val arbitrarySquare: Arbitrary[Square] =
    Arbitrary(Gen.oneOf(Square.allAsSeq))

  implicit val arbitraryVec: Arbitrary[Vec] =
    Arbitrary {
      for {
        file <- arbitrary[Int]
        rank <- arbitrary[Int]
      } yield Vec(file, rank)
    }
}
