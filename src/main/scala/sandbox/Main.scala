package sandbox

import sandbox.show.Show._
import cats.implicits._

object Main extends App {
  println("Hello " |+| "Cats!")

  /** Printable.print(Cat("vasya", 12, "grey"))
    * Cat("Boniface", 12, "grey").print
    */
  println(Cat("Boniface", 12, "grey").show)
}