package sandbox.printable

import sandbox.Cat

/**
  * Created by oleg on 21/01/2018.
  */
trait Printable[A] {
  def format(value: A): String
}
/**
  * Interface  object which expose type class functionality
  */
object Printable {

  def format[A](value: A)(implicit printable: Printable[A]): String = {
    printable.format(value)
  }

  def print[A](value: A)(implicit printable: Printable[A]): Unit = {
    println(format(value))
  }
}



/**
  * Implicit Type Class  instances
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
  * Interface Syntax which provides extension methods (as alternative to Interface Object)
  * implicitly creates a PrintableOps container with all methods for A
  * so we can use it basically :  Cat("Boniface", 12, "grey").format
  */
object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    def format(implicit p: Printable[A]): String = p.format(value)
    def print(implicit p: Printable[A]): Unit = println(p.format(value))
  }

}