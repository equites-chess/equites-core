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
    build empty square boards of size n     $e1
    build non-empty square boards of size 8 $e2
  """

  def e1 =
    |>(|>(☐, ☐, ☐),
       |>(☐, ☐, ☐),
       |>(☐, ☐, ☐)).board must_== Board.empty

  def e2 =
    |>(|>(♜, ♞, ♝, ♛, ♚, ♝, ♞, ♜),
       |>(♟, ♟, ♟, ♟, ♟, ♟, ♟, ♟),
       |>(☐, ☐, ☐, ☐, ☐, ☐, ☐, ☐),
       |>(☐, ☐, ☐, ☐, ☐, ☐, ☐, ☐),
       |>(☐, ☐, ☐, ☐, ☐, ☐, ☐, ☐),
       |>(☐, ☐, ☐, ☐, ☐, ☐, ☐, ☐),
       |>(♙, ♙, ♙, ♙, ♙, ♙, ♙, ♙),
       |>(♖, ♘, ♗, ♕, ♔, ♗, ♘, ♖)).board8x8 must_== Rules.startingBoard

  // Board is not square
  illTyped("""
    |>(|>(R, N),
       |>(P)).board""")

  // Board is not of size 3x3
  illTyped("""
    |>(|>(R, N),
       |>(P, P)).board[_3]""")

  // Board is not of size 8x8
  illTyped("""
    |>(|>(♜, ♞, ♝, ♛, ♚, ♝, ♞, ♜),
       |>(♟, ♟, ♟, ♟, ♟, ♟, ♟, ♟),
       |>(☐, ☐, ☐, ☐, ☐, ☐, ☐, ☐),
       |>(☐, ☐, ☐, ☐, ☐, ☐, ☐, ☐),
       |>(☐, ☐, ☐, ☐, ☐, ☐, ☐, ☐),
       |>(♙, ♙, ♙, ♙, ♙, ♙, ♙, ♙),
       |>(♖, ♘, ♗, ♕, ♔, ♗, ♘, ♖)).board8x8""")
}
