package eu.timepit.equites

import scala.language.higherKinds

import org.scalacheck.Arbitrary
import org.specs2._
import org.specs2.specification._
import scalaz._
import Scalaz._
import scalaz.scalacheck.ScalazProperties._

trait ScalazLaws extends Specification with ScalaCheck {
  def satisfyEqualLaws0[A]
      (implicit e1: Arbitrary[A],
                e2: Equal[A] = null) =
    Option(e2).as {
      descr("Equal") ! check(equal.laws[A])
    }

  def satisfyEqualLaws1[A[_]]
      (implicit e1: Arbitrary[A[Int]],
                e2: Equal[A[Int]] = null) =
    Option(e2).as {
      descr("Equal") ! check(equal.laws[A[Int]])
    }

  def satisfyFunctorLaws1[A[_]]
      (implicit e1: Arbitrary[A[Int]],
                e2: Equal[A[Int]] = null,
                e3: Functor[A]    = null) =
    List(Option(e2), Option(e3)).sequence.as {
      descr("Functor") ! check(functor.laws[A])
    }

  def satisfyMonoidLaws0[A]
      (implicit e1: Arbitrary[A],
                e2: Equal[A]  = null,
                e3: Monoid[A] = null) =
    List(Option(e2), Option(e3)).sequence.as {
      descr("Monoid") ! check(monoid.laws[A])
    }

  def satisfyOrderLaws0[A]
      (implicit e1: Arbitrary[A],
                e2: Order[A] = null) =
    Option(e2).as {
      descr("Order") ! check(order.laws[A])
    }

  def satisfyAllLaws0[A]
      (implicit ev0: Arbitrary[A],
                ev1: Equal[A]  = null,
                ev2: Monoid[A] = null,
                ev3: Order[A]  = null): Fragments =
    flattenExamples(
      satisfyEqualLaws0[A],
      satisfyMonoidLaws0[A],
      satisfyOrderLaws0[A])

  def satisfyAllLaws1[A[_]]
      (implicit ev0: Arbitrary[A[Int]],
                ev1: Equal[A[Int]] = null,
                ev2: Functor[A]    = null): Fragments =
    flattenExamples(
      satisfyEqualLaws1[A],
      satisfyFunctorLaws1[A])

  private def flattenExamples(exs: Option[Example]*): Fragments =
    Fragments.create(exs.flatten: _*)
  
  private def descr(typeclass: String): String =
    s"satisfy the $typeclass laws"
}
