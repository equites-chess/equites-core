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

import org.specs2.mutable._

import util.PieceAbbr.Wiki._
import Rules._

class RulesSpec extends Specification {
  "Rules" should {
    "correctly perform squaresInDirection" in {
      squaresInDirection(Square.unsafeFrom(3, 3), Vec(1, 1)).toList must_==
        List(Square.unsafeFrom(4, 4), Square.unsafeFrom(5, 5), Square.unsafeFrom(6, 6), Square.unsafeFrom(7, 7))
      squaresInDirection(Square.unsafeFrom(3, 3), Vec(-1, 0)).toList must_==
        List(Square.unsafeFrom(2, 3), Square.unsafeFrom(1, 3), Square.unsafeFrom(0, 3))
      squaresInDirection(Square.unsafeFrom(3, 3), Vec(3, 1)).toList must_==
        List(Square.unsafeFrom(6, 4))
    }

    "correctly perform undirectedReachableSquares" in {
      undirectedReachableSquares(Placed(kl, Square.unsafeFrom(3, 2))).toSet must_==
        Set(Square.unsafeFrom(3, 3), Square.unsafeFrom(4, 3), Square.unsafeFrom(4, 2), Square.unsafeFrom(4, 1),
          Square.unsafeFrom(3, 1), Square.unsafeFrom(2, 1), Square.unsafeFrom(2, 2), Square.unsafeFrom(2, 3))

      undirectedReachableSquares(Placed(pl, Square.unsafeFrom(3, 3))).toSet must_==
        Set(Square.unsafeFrom(3, 4))
      undirectedReachableSquares(Placed(pl, Square.unsafeFrom(0, 1))).toSet must_==
        Set(Square.unsafeFrom(0, 2), Square.unsafeFrom(0, 3))
      undirectedReachableSquares(Placed(pd, Square.unsafeFrom(0, 6))).toSet must_==
        Set(Square.unsafeFrom(0, 5), Square.unsafeFrom(0, 4))
    }

    "correctly perform undirectedReachableSquares for Bishop" in {
      undirectedReachableSquares(Placed(bl, Square.unsafeFrom(3, 3))).toSet must_==
        Set(Square.unsafeFrom(0, 0), Square.unsafeFrom(1, 1), Square.unsafeFrom(2, 2), Square.unsafeFrom(4, 4),
          Square.unsafeFrom(5, 5), Square.unsafeFrom(6, 6), Square.unsafeFrom(7, 7),
          Square.unsafeFrom(0, 6), Square.unsafeFrom(1, 5), Square.unsafeFrom(2, 4), Square.unsafeFrom(4, 2),
          Square.unsafeFrom(5, 1), Square.unsafeFrom(6, 0))
    }

    "determine associated castlings for placed pieces" in {
      associatedCastlings(Placed(rl, Square.unsafeFrom(0, 0))) must_==
        Seq(CastlingLong(White))
      associatedCastlings(Placed(rl, Square.unsafeFrom(3, 5))) must_==
        Seq.empty
      associatedCastlings(Placed(rd, Square.unsafeFrom(7, 7))) must_==
        Seq(CastlingShort(Black))
      associatedCastlings(Placed(kd, Square.unsafeFrom(4, 2))) must_==
        Seq(CastlingShort(Black), CastlingLong(Black))
      associatedCastlings(Placed(ql, Square.unsafeFrom(0, 0))) must_==
        Seq.empty
    }
  }
}
