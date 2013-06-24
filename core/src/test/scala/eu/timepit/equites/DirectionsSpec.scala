// Equites, a Scala chess playground
// Copyright © 2011-2013 Frank S. Thomas <frank@timepit.eu>
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

import Directions._

class DirectionsSpec extends Specification {
  "Directions" should {
    "correctly perform inverse" in {
      Directions.front.inverse must_== Directions.back
      Directions.right.inverse must_== Directions.left

      frontRight.inverse must_== backLeft
      frontLeft.inverse  must_== backRight

      diagonalFront.inverse must_== diagonalBack
      forward.inverse       must_== backward

      straight.inverse   must_== straight
      diagonal.inverse   must_== diagonal
      anywhere.inverse   must_== anywhere
      knightLike.inverse must_== knightLike
    }

    "all straight directions are straight" in { straight.forall(_.isStraight) }
    "all diagonal directions are diagonal" in { diagonal.forall(_.isDiagonal) }

    "correctly perform inverseIfWhite" in {
      front.inverseIfWhite(White) must_== back
      front.inverseIfWhite(Black) must_== front
    }

    "correctly perform inverseIfBlack" in {
      front.inverseIfBlack(White) must_== front
      front.inverseIfBlack(Black) must_== back
    }

    "must be comparable to Set[Vector]" in {
      anywhere equals anywhere.self must beTrue
      anywhere.self equals anywhere must beTrue
      anywhere diff diagonal must_== straight
    }

    "correctly perform mostSimilarTo" in {
      diagonal.mostSimilarTo(Vec.front) must_== diagonalFront
      diagonal.mostSimilarTo(Vec.back)  must_== diagonalBack
    }
  }
}
