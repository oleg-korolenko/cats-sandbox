package cases

import cats.instances.int._ // for Monoid
import cats.instances.string._ // for Monoid

import cats.Monoid
import cats.Monoid
import cats.Foldable
import cats.Traverse
import cats.syntax.semigroup._
import cats.instances.int._    // for Monoid
import cats.instances.future._ // for Applicative and Monad
import cats.instances.vector._ // for Foldable and Traverse

import cats.syntax.semigroup._ // for |+|
import cats.syntax.foldable._  // for combineAll and foldMap
import cats.syntax.traverse._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by okorolenko on 08/02/2018.
  */
object MapReduce {
  def foldMap[A, B: Monoid](seq: Vector[A])(func: A => B): B = {
    seq.map(func).foldLeft(Monoid[B].empty)(_ |+| _)
  }

  def foldMapInParallel[A, B: Monoid](seq: Vector[A])(func: A => B): Future[B] = {
    val cpus = Runtime.getRuntime.availableProcessors
    val perCpu = (seq.size / cpus).ceil.toInt
    val groupedFeatures: Iterator[Vector[A]] = seq.grouped(perCpu)
    //create feature per group of cpus
    val futures: Iterator[Future[B]] = groupedFeatures.map {
      group =>
        Future {
          foldMap(group)(func)
        }
    }
    // we can sequence feature resolution
    Future.sequence(futures).map {
      future => future.foldLeft(Monoid[B].empty)(_ |+| _)
    }

  }

  def foldMapInParallelWithCats[A, B: Monoid](seq: Vector[A])(func: A => B): Future[B] = {
    val cpus = Runtime.getRuntime.availableProcessors
    val perCpu = (seq.size / cpus).ceil.toInt
    val groupedFeatures: Iterator[Vector[A]] = seq.grouped(perCpu)
    groupedFeatures.toVector
      .traverse(
        gr =>Future(gr.toVector.foldMap(func)))
          .map(_.combineAll)
  }
}

object MapReduceMain extends App {


  import MapReduce._

  println(foldMap(Vector(1, 2, 3))(identity))
  // res2: Int = 6

  // Mapping to a String uses the concatenation monoid:
  println(foldMap(Vector(1, 2, 3))(_.toString + "! "))

  println(foldMap("Hello world!".toVector)(_.toString.toUpperCase))

  val res = Await.result(foldMapInParallel((1 to 1000000).toVector)(identity), 1.second)
  println(s"foldMapInParallel result = $res")
}
