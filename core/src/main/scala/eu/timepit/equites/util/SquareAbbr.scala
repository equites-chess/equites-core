// Equites, a Scala chess playground
// Copyright © 2014 Frank S. Thomas <frank@timepit.eu>
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

import Square._

object SquareAbbr {
  def a_ : File = File(0)
  def b_ : File = File(1)
  def c_ : File = File(2)
  def d_ : File = File(3)
  def e_ : File = File(4)
  def f_ : File = File(5)
  def g_ : File = File(6)
  def h_ : File = File(7)

  def _1: Rank = Rank(0)
  def _2: Rank = Rank(1)
  def _3: Rank = Rank(2)
  def _4: Rank = Rank(3)
  def _5: Rank = Rank(4)
  def _6: Rank = Rank(5)
  def _7: Rank = Rank(6)
  def _8: Rank = Rank(7)

  def a1: Square = unsafeFrom(a_, _1)
  def a2: Square = unsafeFrom(a_, _2)
  def a3: Square = unsafeFrom(a_, _3)
  def a4: Square = unsafeFrom(a_, _4)
  def a5: Square = unsafeFrom(a_, _5)
  def a6: Square = unsafeFrom(a_, _6)
  def a7: Square = unsafeFrom(a_, _7)
  def a8: Square = unsafeFrom(a_, _8)

  def b1: Square = unsafeFrom(b_, _1)
  def b2: Square = unsafeFrom(b_, _2)
  def b3: Square = unsafeFrom(b_, _3)
  def b4: Square = unsafeFrom(b_, _4)
  def b5: Square = unsafeFrom(b_, _5)
  def b6: Square = unsafeFrom(b_, _6)
  def b7: Square = unsafeFrom(b_, _7)
  def b8: Square = unsafeFrom(b_, _8)

  def c1: Square = unsafeFrom(c_, _1)
  def c2: Square = unsafeFrom(c_, _2)
  def c3: Square = unsafeFrom(c_, _3)
  def c4: Square = unsafeFrom(c_, _4)
  def c5: Square = unsafeFrom(c_, _5)
  def c6: Square = unsafeFrom(c_, _6)
  def c7: Square = unsafeFrom(c_, _7)
  def c8: Square = unsafeFrom(c_, _8)

  def d1: Square = unsafeFrom(d_, _1)
  def d2: Square = unsafeFrom(d_, _2)
  def d3: Square = unsafeFrom(d_, _3)
  def d4: Square = unsafeFrom(d_, _4)
  def d5: Square = unsafeFrom(d_, _5)
  def d6: Square = unsafeFrom(d_, _6)
  def d7: Square = unsafeFrom(d_, _7)
  def d8: Square = unsafeFrom(d_, _8)

  def e1: Square = unsafeFrom(e_, _1)
  def e2: Square = unsafeFrom(e_, _2)
  def e3: Square = unsafeFrom(e_, _3)
  def e4: Square = unsafeFrom(e_, _4)
  def e5: Square = unsafeFrom(e_, _5)
  def e6: Square = unsafeFrom(e_, _6)
  def e7: Square = unsafeFrom(e_, _7)
  def e8: Square = unsafeFrom(e_, _8)

  def f1: Square = unsafeFrom(f_, _1)
  def f2: Square = unsafeFrom(f_, _2)
  def f3: Square = unsafeFrom(f_, _3)
  def f4: Square = unsafeFrom(f_, _4)
  def f5: Square = unsafeFrom(f_, _5)
  def f6: Square = unsafeFrom(f_, _6)
  def f7: Square = unsafeFrom(f_, _7)
  def f8: Square = unsafeFrom(f_, _8)

  def g1: Square = unsafeFrom(g_, _1)
  def g2: Square = unsafeFrom(g_, _2)
  def g3: Square = unsafeFrom(g_, _3)
  def g4: Square = unsafeFrom(g_, _4)
  def g5: Square = unsafeFrom(g_, _5)
  def g6: Square = unsafeFrom(g_, _6)
  def g7: Square = unsafeFrom(g_, _7)
  def g8: Square = unsafeFrom(g_, _8)

  def h1: Square = unsafeFrom(h_, _1)
  def h2: Square = unsafeFrom(h_, _2)
  def h3: Square = unsafeFrom(h_, _3)
  def h4: Square = unsafeFrom(h_, _4)
  def h5: Square = unsafeFrom(h_, _5)
  def h6: Square = unsafeFrom(h_, _6)
  def h7: Square = unsafeFrom(h_, _7)
  def h8: Square = unsafeFrom(h_, _8)
}
