// Equites, a Scala chess playground
// Copyright © 2011, 2013-2015 Frank S. Thomas <frank@timepit.eu>
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

import eu.timepit.equites.Movement._
import eu.timepit.equites.util.BoardBuilder._
import eu.timepit.equites.util.PieceAbbr.Algebraic._
import eu.timepit.equites.util.PieceAbbr.Wiki._
import eu.timepit.equites.util.SquareAbbr._
import org.specs2.mutable._

class MovementSpec extends Specification {
  "Movement" should {
    "correctly perform squaresInDirection" in {
      squaresInDirection(d4, Vec(+1, 1)).toList must_== List(e5, f6, g7, h8)
      squaresInDirection(d4, Vec(-1, 0)).toList must_== List(c4, b4, a4)
      squaresInDirection(d4, Vec(+3, 1)).toList must_== List(g5)
    }

    "correctly perform undirectedReachableSquares" in {
      undirectedReachableSquares(Placed(kl, d3)).toSet must_==
        Set(c2, c3, c4, d2, d4, e2, e3, e4)

      undirectedReachableSquares(Placed(pl, d4)).toSet must_== Set(d5)
      undirectedReachableSquares(Placed(pl, a2)).toSet must_== Set(a3, a4)
      undirectedReachableSquares(Placed(pd, a7)).toSet must_== Set(a6, a5)
    }

    "correctly perform undirectedReachableSquares for Bishop" in {
      undirectedReachableSquares(Placed(bl, d4)).toSet must_==
        Set(a1, b2, c3, e5, f6, g7, h8, a7, b6, c5, e3, f2, g1)
    }

    "find reachable vacant and occupied squares" in {
      val board = >>.
        |.|.q.|.|.|.|.P.
        |.|.|.|.|.|.R.|.
        |.|.|.|.|.|.|.|.
        |.|.|.|.|.|.|.|.
        B.|.|.|.p.p.|.|.
        |.|.|.|.P.|.|.|.
        |.|.|.|.|.|.|.|.
        Q.|.n.|.|.k.|.|.<<

      val queen = Placed(ql, a1)

      reachableVacantSquares(queen, board).toSet must_==
        Set(a2, a3, b1, b2, c3, d4, e5, f6)

      reachableOccupiedSquares(queen, board).toSet must_==
        Set(Placed(B, a4), Placed(R, g7), Placed(n, c1))

      reachableOccupiedSquares(Placed(P, e3), board).toSet must_==
        Set(Placed(pd, f4))
    }
  }
}
