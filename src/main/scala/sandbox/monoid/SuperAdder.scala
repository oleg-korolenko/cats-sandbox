package sandbox.monoid

import cats.instances.int._
import cats.kernel.Monoid
import cats.syntax.semigroup._


object SuperAdder {
  def addInt(items: List[Int]): Int = {
    items.fold(Monoid[Int].empty)(_ |+| _)
  }
  // catching the type and inject the corresponding Monoid
  def add[A](items: List[A])(implicit monoid: Monoid[A]): A = {
    items.fold(monoid.empty)(_ |+| _)
  }
}