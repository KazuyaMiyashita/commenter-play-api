package v0.models.tables

import scalikejdbc._
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import util.Try

import v0.models.forms.AuthForm

object AuthsTable {

  ConnectionPool.singleton("jdbc:mysql://127.0.0.1:3306/commenter", "root", "")
  implicit val session = AutoSession

  val bcrypt = new BCryptPasswordEncoder()
  def createHash(password: String): String = bcrypt.encode(password)

  def save(data: AuthForm): Try[Int] = Try {
    val username = data.username
    val hashedPassword = createHash(data.password)
    sql"insert into auths (username, password) values (${data.username}, ${hashedPassword})".update.apply()
  }

}
