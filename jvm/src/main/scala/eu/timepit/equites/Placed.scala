// Equites, a Scala chess playground
// Copyright © 2013-2015 Frank S. Thomas <frank@timepit.eu>
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

import scalaz._

case class Placed[+A](elem: A, square: Square) {
  def map[B](f: A => B): Placed[B] = copy(f(elem))
  def toTuple: (Square, A) = (square, elem)
}

object Placed extends PlacedInstances

trait PlacedInstances {
  implicit def placedEqual[A]: Equal[Placed[A]] =
    Equal.equalA[Placed[A]]

  implicit val placedFunctor: Functor[Placed] =
    new Functor[Placed] {
      def map[A, B](fa: Placed[A])(f: A => B): Placed[B] =
        fa.map(f)
    }

  implicit def placedOrder[A]: Order[Placed[A]] =
    Order.orderBy(_.square)

  implicit def placedScalaOrdering[A]: scala.Ordering[Placed[A]] =
    placedOrder[A].toScalaOrdering

  implicit def placedShow[A]: Show[Placed[A]] =
    Show.showFromToString[Placed[A]]
}

trait PlacedTypeAliases {
  type PlacedPiece = Placed[AnyPiece]
}
