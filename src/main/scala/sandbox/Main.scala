package sandbox


import cats.implicits._
import sandbox.monoid.SuperAdder

object Main extends App {
  println("Hello " |+| "Cats!")
  /** Printable.print(Cat("vasya", 12, "grey"))
    * Cat("Boniface", 12, "grey").print
    */
  //println (Cat("Boniface", 12, "grey").some === Cat("Boniface", 12, "grey").some)
  import cats.instances.option._

  println(s"SuperAdder says = ${SuperAdder.addInt(List(1, 2, 3))})")
  println(s"SuperAdder says = ${SuperAdder.add(List(Some(1), None, Some(2)))})")


  /** println(s"SuperAdder says = ${SuperAdder.add(List(Order(1.0, 2.0), Order(3.0, 4.0)))})") */

  import sandbox.functor.Functors._

  println(Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2))
}