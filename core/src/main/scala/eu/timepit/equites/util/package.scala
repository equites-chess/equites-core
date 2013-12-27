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

import java.nio.charset.Charset
import scala.collection.immutable.NumericRange

package object util {
  def backtracking[C](firstCandidate: C)(nextCandidates: C => Stream[C],
                                         accept: C => Boolean): Stream[C] = {
    def recur(c: C): Stream[C] = nextCandidates(c).flatMap {
      next => if (accept(next)) Stream(next) else recur(next)
    }
    recur(firstCandidate)
  }

  def incrRange(range: Range, incr: Int): Range =
    (range.head + incr) to (range.last + incr) by range.step

  def toCharRange(range: Range, offset: Char): NumericRange[Char] = {
    val start = (offset + range.head).toChar
    val end = (offset + range.last).toChar
    start to end by range.step.toChar
  }

  def toStringOnOff(bool: Boolean): String = if (bool) "on" else "off"

  def toUtf8Bytes[A](a: A): Array[Byte] = a.toString.getBytes(utf8Charset)
  def toUtf8BytesLf[A](a: A): Array[Byte] = toUtf8Bytes(a.toString + "\n")

  private val utf8Charset: Charset = Charset.forName("UTF-8")
}
