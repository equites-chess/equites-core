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

import implicits.DrawImplicits._
import util.CoordinateMove
import util.PieceAbbr.Wiki._
import util.SquareAbbr._
import Uci._

import ArbitraryInstances._

class UciParsersSpec extends Specification with ParserMatchers with ScalaCheck {
  val parsers = UciParsers
  import parsers._

  "square" should {
    "succeed on random algebraic squares" in check {
      (s: Square) => square should succeedOn(util.SquareUtil.showAlgebraic(s)).withResult(s)
    }
  }

  "coordinateMove" should {
    "succeed on a move" in {
      coordinateMove should succeedOn("e2e4")
        .withResult(CoordinateMove(e2 -> e4))
    }
    "succeed on white promotions" in check {
      (p: PromotedPiece) =>
        (p.color == White) ==> {
          val cm = CoordinateMove(e7 -> e8, Some(p))
          coordinateMove should succeedOn(cm.toAlgebraic).withResult(cm)
        }
    }
    "succeed on black promotions" in check {
      (p: PromotedPiece) =>
        (p.color == Black) ==> {
          val cm = CoordinateMove(e2 -> e1, Some(p))
          coordinateMove should succeedOn(cm.toAlgebraic).withResult(cm)
        }
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
      uciok should succeedOn("uciok").withResult(UciOk)
    }
  }

  "readyok" should {
    "succeed on valid input" in {
      readyok should succeedOn("readyok").withResult(ReadyOk)
    }
  }

  "bestmove" should {
    "succeed on a move" in {
      val move = CoordinateMove(e2 -> e4)
      bestmove should succeedOn("bestmove e2e4")
        .withResult(Bestmove(move))
    }
    "succeed on a promotion" in {
      val move = CoordinateMove(e7 -> e8, Some(ql))
      bestmove should succeedOn("bestmove e7e8q")
        .withResult(Bestmove(move))
    }
    "succeed on a move and a ponder" in {
      val move = CoordinateMove(g1 -> f3)
      val ponder = Some(CoordinateMove(d8 -> f6))
      bestmove should succeedOn("bestmove g1f3 ponder d8f6")
        .withResult(Bestmove(move, ponder))
    }
  }

  "option" should {
    import UciOption._

    "succeed on type check" in {
      option should succeedOn("option name Ponder type check default true")
        .withResult(UciOption("Ponder", Check(true)))
    }
    "suceed on type spin" in {
      val str = "option name Hash type spin default 32 min 4 max 4096"
      option should succeedOn(str)
        .withResult(UciOption("Hash", Spin(32, 4, 4096)))
    }
    "suceed on type combo" in {
      val str = "option name Style type combo default Normal var Solid var Normal var Risky"
      option should succeedOn(str)
        .withResult(UciOption("Style", Combo("Normal", Seq("Solid", "Normal", "Risky"))))
    }
    "succeed on type button" in {
      option should succeedOn("option name Clear Hash type button")
        .withResult(UciOption("Clear Hash", Button))
    }
    "succeed on type string" in {
      val str = "option name Book File type string default book.bin"
      option should succeedOn(str)
        .withResult(UciOption("Book File", StringType("book.bin")))
    }
  }

  "response" should {
    "succeed on id" in {
      response should succeedOn("id author John Doe")
      response should succeedOn("uciok")
      response should succeedOn("readyok")
      response should succeedOn("bestmove e2e4")
      response should succeedOn("option name Clear Hash type button")
    }
  }
}
