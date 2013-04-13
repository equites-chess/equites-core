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
package implicits

import scala.collection.{IterableLike, TraversableLike}
import scalaz._

object GenericImplicits {
  implicit final class CollectionOps[C](val self: C) extends AnyVal {
    def asOption(implicit C: Unapply[IsEmpty, C]): Option[C] =
      if (C.TC.isEmpty(C(self))) None else Some(self)

    def dropLeftRight(n: Int)(implicit ev: C => IterableLike[_, C]): C =
      self.drop(n).dropRight(n)

    def minGroupBy[A, B](f: A => B)
        (implicit ev0: C => TraversableLike[A, C],
                  ev1: scala.Ordering[B]): C = {
      if (self.isEmpty) self else self.groupBy(f).minBy(_._1)._2
    }
  }
}
