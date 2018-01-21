package sandbox.printable

import sandbox.Cat

/**
  * Created by oleg on 21/01/2018.
  */
object Printable {

  def format[A](value: A)(implicit printable: Printable[A]): String = {
    printable.format(value)
  }

  def print[A](value: A)(implicit printable: Printable[A]): Unit = {
    println(format(value))
  }
}

trait Printable[A] {
  def format(value: A): String
}

/**
  * as implicit instances
  */
object PrintableInstances {
  implicit val stringPrintable: Printable[String] = new Printable[String] {
    override def format(value: String): String = value
  }
  implicit val intPrintable: Printable[Int] = new Printable[Int] {
    override def format(value: Int): String = value.toString
  }
  implicit val catPrintable: Printable[Cat] = new Printable[Cat] {
    override def format(value: Cat): String =
      s"${value.name} is a ${value.age} year-old ${value.color} cat"
  }
}

/**
  * same with extension methods (as syntax)
  * implicitly creates a PrintableOps container with all methods for A
  * so we can use it basically :  Cat("Boniface", 12, "grey").format
  */
object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    def format(implicit p: Printable[A]): String = p.format(value)
    def print(implicit p: Printable[A]): Unit = println(p.format(value))
  }

}