package sandbox.eq

import sandbox.Cat
import cats._
import cats.implicits._

object Eq {
  implicit val catE: Eq[Cat] = cats.Eq.instance[Cat] {
    (cat1, cat2) => {
      cat1.age === cat2.age &&
        cat1.name === cat2.name &&
        cat1.color === cat2.color
    }
  }
}