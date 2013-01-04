// Equites, a simple chess interface
// Copyright © 2011 Frank S. Thomas <f.thomas@gmx.de>
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

import scala.collection._

class History private (val past: List[Action], val future: List[Action])
  extends Iterable[Action] {

  def this() = this(Nil, Nil)

  def hasPrev: Boolean = past   nonEmpty
  def hasNext: Boolean = future nonEmpty

  def prev: Action = past   head
  def next: Action = future head

  def prevOption: Option[Action] = past   headOption
  def nextOption: Option[Action] = future headOption

  def backward: History = {
    if (past isEmpty) this
    else new History(past.tail, past.head :: future)
  }

  def forward: History = {
    if (future isEmpty) this
    else new History(future.head :: past, future.tail)
  }

  def record(action: Action): History = new History(action :: past, Nil)

  def iterator: Iterator[Action] =
    past.reverseIterator ++ future.iterator

  def reverseIterator: Iterator[Action] =
    future.reverseIterator ++ past.iterator
}
