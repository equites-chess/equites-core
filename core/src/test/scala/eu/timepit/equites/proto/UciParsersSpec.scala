// Equites, a Scala chess playground
// Copyright © 2013 Frank S. Thomas <frank@timepit.eu>
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
package proto

import org.specs2.ScalaCheck
import org.specs2.mutable._
import org.specs2.matcher.ParserMatchers

import Uci._
import implicits.SquareImplicits._
import util.CoordinateMove
import util.PieceAbbr._

class UciParsersSpec extends Specification with ParserMatchers with ScalaCheck {
  val parsers = UciParsers
  import parsers._

  "square" should {
    "succeed on random algebraic squares" in check {
      (s: Square) => square should succeedOn(s.toAlgebraic).withResult(s)
    }
  }

  "coordinateMove" should {
    "succeed on a move" in {
      coordinateMove should succeedOn("e2e4")
        .withResult(CoordinateMove(Square('e', 2), Square('e', 4)))
    }
    "succeed on a white queen promotion" in {
      val promotion = CoordinateMove(Square('e', 7), Square('e', 8), Some(Q))
      coordinateMove should succeedOn("e7e8q")
        .withResult(promotion)
    }
    "succeed on a white knight promotion" in {
      val promotion = CoordinateMove(Square('e', 7), Square('e', 8), Some(N))
      coordinateMove should succeedOn("e7e8n")
        .withResult(promotion)
    }
    "succeed on a black queen promotion" in {
      val promotion = CoordinateMove(Square('e', 2), Square('e', 1), Some(q))
      coordinateMove should succeedOn("e2e1q")
        .withResult(promotion)
    }
    "succeed on a black knight promotion" in {
      val promotion = CoordinateMove(Square('e', 2), Square('e', 1), Some(n))
      coordinateMove should succeedOn("e2e1n")
        .withResult(promotion)
    }
  }

  "id" should {
    "succeed on valid input" in {
      id should succeedOn("id name Engine X Y")
        .withResult(Id("name", "Engine X Y"))
      id should succeedOn("id author John Doe")
        .withResult(Id("author", "John Doe"))
    }
    "succeed on empty value" in {
      id should succeedOn("id name").withResult(Id("name", ""))
    }
    "fail on missing key" in {
      id should failOn("id")
    }
  }

  "uciok" should {
    "succeed on valid input" in {
      uciok should succeedOn("uciok")
    }
  }

  "readyok" should {
    "succeed on valid input" in {
      readyok should succeedOn("readyok")
    }
  }

  "bestmove" should {
    "succeed on a move" in {
      val move = CoordinateMove(Square('e', 2), Square('e', 4))
      bestmove should succeedOn("bestmove e2e4")
        .withResult(Bestmove(move))
    }
    "succeed on a promotion" in {
      val move = CoordinateMove(Square('e', 7), Square('e', 8), Some(Q))
      bestmove should succeedOn("bestmove e7e8q")
        .withResult(Bestmove(move))
    }
    "succeed on a move and a ponder" in {
      val move = CoordinateMove(Square('g', 1), Square('f', 3))
      val ponder = Some(CoordinateMove(Square('d', 8), Square('f', 6)))
      bestmove should succeedOn("bestmove g1f3 ponder d8f6")
        .withResult(Bestmove(move, ponder))
    }
  }
}
