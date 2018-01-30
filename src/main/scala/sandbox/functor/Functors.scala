package sandbox.functor

import cats.Functor
import sandbox.{Branch, Leaf, Tree}

/**
  * Created by okorolenko on 30/01/2018.
  */
object Functors {
  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = tree match {
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
      case Leaf(value) => Leaf(f(value))
    }
  }
}
