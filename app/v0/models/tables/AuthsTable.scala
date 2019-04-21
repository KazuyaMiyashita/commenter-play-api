package v0.models.tables

import play.api.Configuration

import scalikejdbc._

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import de.huxhorn.sulky.ulid.ULID;

import util.{Try, Success, Failure}

import v0.models.entities.Auth
import v0.models.forms.{CreateUserForm, AuthLoginForm}

class AuthsTable(private val config: Configuration) {

  ConnectionPool.singleton(
    config.get[String]("db.url"),
    config.get[String]("db.username"),
    config.get[String]("db.password")
  )
  implicit val session = AutoSession

  import AuthsTable._

  def save(form: CreateUserForm): Try[Unit] = Try {
    val auth_id: String = createULID()
    val email = form.email
    val hashedPassword = createHash(form.rawPassword)
    val user_id: String = createULID()
    val name = form.name
    sql"insert into auths (id, email, password) values (${auth_id}, ${email}, ${hashedPassword})"
      .update.apply()
    sql"insert into users (id, auth_id, name) values (${user_id}, ${auth_id}, ${name})"
      .update.apply()
  }

  def login(form: AuthLoginForm): Try[String] = {
    def mkAuthEntity(rs: WrappedResultSet) = Auth(
      id = rs.get("id"),
      email = rs.get("email"),
      hashedPassword = rs.get("password")
    )

    val email = form.email
    val rawPassword = form.rawPassword

    Try {
      sql"select id, email, password from auths where email = ${email}"
        .map(rs => mkAuthEntity(rs))
        .single.apply()
    } flatMap { 
      case Some(entity) => Success(entity)
      case None => Failure(new NonExistUserException(email))
    } flatMap {
      case entity: Auth => {
        val hashedPassword = entity.hashedPassword
        if (authenticate(rawPassword, hashedPassword)) Success(entity)
        else Failure(new InvalidPasswordException)
      }
    } flatMap {
      case entity: Auth => Try {
        val auth_id = entity.id
        val token = AuthsTable.createToken(java.util.Calendar.getInstance.getTimeInMillis, email)
        sql"insert into tokens (token, auth_id, created_at) values (${token}, ${auth_id}, current_timestamp)"
          .update.apply()
        token
      }
    }
  }

  def logout(): Try[Unit] = Try {

  }

}

class AuthsTableException extends Exception
class NonExistUserException(username: String) extends AuthsTableException
class InvalidPasswordException extends AuthsTableException

object AuthsTable {

  val bcrypt = new BCryptPasswordEncoder()
  def createHash(password: String): String = bcrypt.encode(password)
  def authenticate(rawPassword: String, hashedPassword: String): Boolean =
    bcrypt.matches(rawPassword, hashedPassword)

  def createULID(): String = (new ULID).nextULID

  def createToken(currentTimeMills: Long, anotherSeed: Any): String = {
    import util.Random
    val rnd = new Random
    rnd.setSeed(currentTimeMills + anotherSeed.##)

    val ts = (('A' to 'Z').toList :::
      ('a' to 'z').toList :::
      ('0' to '9').toList :::
      List('-', '.', '_', '~', '+', '/'))
      .toArray

    val tsLen = ts.length
    val length = 64

    "Bearer " + List.fill(length)(ts(rnd.nextInt(tsLen))).mkString
  }


}
