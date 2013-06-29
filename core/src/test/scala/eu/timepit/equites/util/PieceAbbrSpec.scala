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
package util

import org.specs2.mutable._

import implicits.PieceImplicits._
import PieceAbbr._

class PieceAbbrSpec extends Specification with Tables {
  "PieceAbbr" should {
    "contain all single letter abbreviations" in {
      "abbr" | "letter" |
      K      ! "K"      |
      Q      ! "Q"      |
      R      ! "R"      |
      B      ! "B"      |
      N      ! "N"      |
      P      ! "P"      |
      k      ! "k"      |
      q      ! "q"      |
      r      ! "r"      |
      b      ! "b"      |
      n      ! "n"      |
      PieceAbbr.p ! "p" |> {
        (piece, string) => piece.toLetter must_== string
      }
    }

    "contain all MediaWiki abbreviations" in {
      "abbr" | "letters" |
      kl     ! "kl"      |
      ql     ! "ql"      |
      rl     ! "rl"      |
      bl     ! "bl"      |
      nl     ! "nl"      |
      pl     ! "pl"      |
      kd     ! "kd"      |
      qd     ! "qd"      |
      rd     ! "rd"      |
      bd     ! "bd"      |
      nd     ! "nd"      |
      pd     ! "pd"      |> {
        (piece, string) => piece.toWikiLetters must_== string
      }
    }
  }
}
