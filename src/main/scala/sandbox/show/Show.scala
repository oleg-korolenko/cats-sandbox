package sandbox.show
import cats._
import sandbox.Cat

object Show {
  implicit val catShow: Show[Cat] =
    cats.Show.show[Cat](value => s"${value.name} is a ${value.age} year-old ${value.color} cat")
}
