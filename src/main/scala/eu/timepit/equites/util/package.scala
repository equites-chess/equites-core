// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import scala.collection.immutable.NumericRange
import scalaz.Monad
import scalaz.syntax.monad._

package object util {
  def backtrack[C, M[_]: Monad](firstCandidate: C)(nextCandidates: C => M[C],
                                                   accept: C => Boolean): M[C] = {
    def traverse(c: C): M[C] = nextCandidates(c).flatMap(returnOrTraverse)
    def returnOrTraverse(c: C): M[C] = if (accept(c)) c.point else traverse(c)
    returnOrTraverse(firstCandidate)
  }

  def incrRange(range: Range, incr: Int): Range =
    (range.head + incr) to (range.last + incr) by range.step

  def toCharRange(range: Range, offset: Char): NumericRange[Char] = {
    val start = (offset + range.head).toChar
    val end = (offset + range.last).toChar
    val step = range.step.toChar
    start to end by step
  }

  def toStringOnOff(bool: Boolean): String = if (bool) "on" else "off"

  def toTuple2[A](a: A): (A, A) = (a, a)
}
