package sandbox.monad

import cats.Monad
import sandbox.{Branch, Leaf, Tree}
import sandbox.Tree._

/**
  * Created by okorolenko on 03/02/2018.
  */


object TreeMonad {
  implicit val treeMonad = new Monad[Tree] {
    override def pure[A](x: A): Tree[A] = Leaf(x)

    override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = {
      fa match {
        case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
        case Leaf(value) => f(value)
      }
    }

    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {
      f(a) match {
        case Leaf(Left(value))=>tailRecM(value)(f)
        case Leaf(Right(value))=>Leaf(value)
        case Branch(left,right) => Branch(
          flatMap(left){
            case Left(left)=>tailRecM(left)(f)
            case Right(left)=>pure(left)
          },
          flatMap(right){
            case Left(right)=>tailRecM(right)(f)
            case Right(right)=>pure(right)
          }
        )
      }
    }
  }

}
import cats.syntax.flatMap._

import TreeMonad._
object TreeMonadMain extends  App{
 val tree = branch(leaf(5),leaf(50))
   println(tree.flatMap(x=>Branch(Leaf(x+10),Leaf(x+100))))
}