package v0.models.tables

import play.api.Configuration

import scalikejdbc._
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import util.Try

import v0.models.entities.Auth
import v0.models.forms.AuthForm

class AuthsTable(private val config: Configuration) {

  ConnectionPool.singleton(
    config.get[String]("db.url"),
    config.get[String]("db.username"),
    config.get[String]("db.password")
  )
  implicit val session = AutoSession

  import AuthsTable._
  // def authenticate(password: String): Boolean = {
  //   val hashString = fromDB()
  //   bcrypt.matches(password, hashString)
  // }

  private def mkAuthEntity(rs: WrappedResultSet) = Auth(
    username = rs.get("username"),
    password = rs.get("password")
  )

  def save(data: AuthForm): Try[Unit] = Try {
    val username = data.username
    val hashedPassword = createHash(data.password)
    sql"insert into auths (username, password) values (${data.username}, ${hashedPassword})".update.apply()
  }

  def login(form: AuthForm): Option[String] = {
    val username = form.username
    val rawPassword = form.password
    val entityOpt: Option[Auth] = sql"select username, password from auths where username = ${username}"
      .map(rs => mkAuthEntity(rs))
      .single.apply()

    val token: Option[String] = entityOpt flatMap { entity =>
      val hashedPassword = entity.password
      if (authenticate(rawPassword, hashedPassword)) Some(entity)
      else None
    } map { entity =>
      val token = AuthsTable.createToken(java.util.Calendar.getInstance.getTimeInMillis, username)

      // DBへの追加処理

      token
    }

    token
  }

}

object AuthsTable {

  val bcrypt = new BCryptPasswordEncoder()
  def createHash(password: String): String = bcrypt.encode(password)
  def authenticate(rawPassword: String, hashedPassword: String): Boolean =
    bcrypt.matches(rawPassword, hashedPassword)

  def createToken(currentTimeMills: Long, anotherSeed: Any): String = {
    import util.Random
    val rnd = new Random
    rnd.setSeed(currentTimeMills + anotherSeed.##)

    val ts = (('A' to 'Z').toList :::
      ('a' to 'z').toList :::
      ('0' to '9').toList :::
      List('=', '.', '_', '~', '+', '/'))
      .toArray

    val tsLen = ts.length
    val length = 32

    "Bearer " + List.fill(length)(ts(rnd.nextInt(tsLen))).mkString
    
  }


}
