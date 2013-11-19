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
import shapeless.nat._
import shapeless.test.illTyped

import SizedBoardBuilder._

class SizedBoardBuilderSpec extends Specification { def is = s2"""
  SizedBoardBuilder should
    build empty boards with 1x1 empty squares $e1
    build empty boards with 8x8 empty squares $e2
    build non-empty boards of size 8x8 using algebraic pieces $e3
    build non-empty boards of size 8x8 using figurine pieces  $e4
  """

  def e1 =
    |>(|>(/)).toBoard must_== Board.empty

  def e2 =
    |>(|>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /)).toBoard8x8 must_== Board.empty

  def e3 =
    |>(|>(r, n, b, q, k, b, n, r),
       |>(♟, ♟, ♟, ♟, ♟, ♟, ♟, ♟),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(P, P, P, P, P, P, P, P),
       |>(R, N, B, Q, K, B, N, R)).toBoard8x8 must_== Rules.startingBoard

  def e4 =
    |>(|>(♜, ♞, ♝, ♛, ♚, ♝, ♞, ♜),
       |>(♟, ♟, ♟, ♟, ♟, ♟, ♟, ♟),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(♙, ♙, ♙, ♙, ♙, ♙, ♙, ♙),
       |>(♖, ♘, ♗, ♕, ♔, ♗, ♘, ♖)).toBoard8x8 must_== Rules.startingBoard

  // Board is not square
  illTyped("""
    |>(|>(♜, ♞),
       |>(♟)).toBoard
    """)

  // Board is square but not of size 3x3
  illTyped("""
    |>(|>(♜, ♞),
       |>(♟, ♟)).toBoard[_3]
    """)

  // Board is not of size 8x8
  illTyped("""
    |>(|>(♜, ♞, ♝, ♛, ♚, ♝, ♞, ♜),
       |>(♟, ♟, ♟, ♟, ♟, ♟, ♟, ♟),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(/, /, /, /, /, /, /, /),
       |>(♙, ♙, ♙, ♙, ♙, ♙, ♙, ♙),
       |>(♖, ♘, ♗, ♕, ♔, ♗, ♘, ♖)).toBoard8x8
    """)
}
