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

package equites

import scala.collection.mutable.ArrayBuffer

class History extends Iterable[Action] {
  def hasPrev: Boolean = present >= 0
  def hasNext: Boolean = timeline.isDefinedAt(present + 1)

  def prev: Option[Action] = get(present)
  def next: Option[Action] = get(present + 1)

  def backward(): Option[Action] = {
    val action = prev
    if (hasPrev) present -= 1
    action
  }

  def forward(): Option[Action] = {
    val action = next
    if (hasNext) present += 1
    action
  }

  def record(action: Action) {
    present += 1
    timeline reduceToSize present
    timeline append action
  }

  def clear() {
    present = -1
    timeline.clear()
  }

  def iterator: Iterator[Action] = timeline.iterator
  def reverseIterator: Iterator[Action] = timeline.reverseIterator

  private def get(idx: Int): Option[Action] =
    if (timeline isDefinedAt idx) Some(timeline(idx)) else None

  private var present: Int = -1
  private val timeline: ArrayBuffer[Action] = ArrayBuffer()
}
