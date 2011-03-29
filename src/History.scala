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

import scala.collection.mutable.Stack

class History {
  def hasLast: Boolean = past.nonEmpty
  def hasNext: Boolean = future.nonEmpty

  def last: Option[Action] = past.headOption
  def next: Option[Action] = future.headOption

  def record(action: Action) {
    past.push(action)
    future.clear()
  }

  def backward(): Option[Action] = move(past, future)
  def forward():  Option[Action] = move(future, past)

  private def move(from: Stack[Action], to: Stack[Action]): Option[Action] = {
    if (from.isEmpty) None
    val action = from.pop()
    to.push(action)
    Some(action)
  }

  private val past:   Stack[Action] = Stack()
  private val future: Stack[Action] = Stack()
}
