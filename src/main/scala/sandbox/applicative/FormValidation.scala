package sandbox.applicative

import cats.data.Validated
import cats.syntax.either._
import cats.instances.list._
import cats.syntax.apply._

/**
  * Created by okorolenko on 05/02/2018.
  */
object FormValidation {
  type FailFast[A] = Either[List[String], A]
  type FailSlow[A] = Validated[List[String], A]

  case class User(name: String, age: Int)

  def readValue(field: String)(params: Map[String, String]): FailFast[String] = {
    params.get(field).toRight(List(s"$field is missing"))
  }

  def parseInt(name: String)(toParse: String): FailFast[Int] =
    Either.catchOnly[NumberFormatException](toParse.toInt)
      .leftMap(_ => List(s"$name is not an int"))

  def nonBlank(name: String)(data: String): FailFast[String] =
    Right(data).
      ensure(List(s"$name cannot be blank"))(_.nonEmpty)

  def nonNegative(name: String)(data: Int): FailFast[Int] =
    Right(data).
      ensure(List(s"$name must be non-negative"))(_ >= 0)

  def readName(data: Map[String, String]): FailFast[String] =
    readValue("name")(data).
      flatMap(nonBlank("name"))

  def readAge(data: Map[String, String]): FailFast[Int] =
    readValue("age")(data).
      flatMap(nonBlank("age")).
      flatMap(parseInt("age")).
      flatMap(nonNegative("age"))

  def readUser(data: Map[String, String]): FailSlow[User] = {
    (
      readName(data).toValidated,
      readAge(data).toValidated
    ).mapN(User.apply)
  }
}

object FormValidationMain extends App {

  import FormValidation._

  println("name=" + readName(Map("name" -> "Jean", "age" -> "12")))
  println("age<0 =" + readAge(Map("age" -> "-1")))
  println("valid user =" + readUser(Map("name" -> "Dave", "age" -> "37")))
}
