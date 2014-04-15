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

import org.specs2._

import PieceAbbr.Algebraic._
import PieceAbbr.Figurine._
import PieceAbbr.Wiki._

class PieceAbbrSpec extends Specification with matcher.DataTables { def is = s2"""
  PieceAbbr.Algebraic should
    contain abbreviations for all pieces $e1

  PieceAbbr.Figurine should
    contain abbreviations for all pieces $e2

  PieceAbbr.Wiki should
    contain abbreviations for all pieces $e3
  """

  def e1 =
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
    PieceAbbr.Algebraic.p ! "p" |> {
      (piece, string) => PieceUtil.showLetter(piece) must_== string
    }

  def e2 =
    "abbr" | "figurine" |
    ♔      ! "♔"        |
    ♕      ! "♕"        |
    ♖      ! "♖"        |
    ♗      ! "♗"        |
    ♘      ! "♘"        |
    ♙      ! "♙"        |
    ♚      ! "♚"        |
    ♛      ! "♛"        |
    ♜      ! "♜"        |
    ♝      ! "♝"        |
    ♞      ! "♞"        |
    ♟      ! "♟"        |> {
      (piece, string) => PieceUtil.showFigurine(piece) must_== string
    }

  def e3 =
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
      (piece, string) => PieceUtil.showWikiLetters(piece) must_== string
    }
}
