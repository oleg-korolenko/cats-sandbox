package sandbox.monad

import cats.data.Reader
// to get pure
import cats.syntax.applicative._


/**
  * Created by okorolenko on 02/02/2018.
  */
object LoginReader {
  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(_.usernames.get(userId))

  def checkPassword(
                     username: String,
                     password: String): DbReader[Boolean] =
    Reader(_.passwords.get(username).contains(password))

  def checkLogin(
                  userId: Int,
                  password: String): DbReader[Boolean] =
    for {
      user <- findUsername(userId)
      pass <- user.map { userName => checkPassword(userName, password)
      }.getOrElse {
        false.pure[DbReader]
      }
    } yield pass
}

case class Db(usernames: Map[Int, String],
              passwords: Map[String, String]
             )

object Main extends App {
  val users = Map(1 -> "joe", 2 -> "kate")
  val passwords = Map("joe" -> "joepass", "kate" -> "katepass")
  val db = Db(users, passwords)
  println(LoginReader.checkLogin(1,"joepass").run(db))
  println(LoginReader.checkLogin(2,"aa").run(db))
}



