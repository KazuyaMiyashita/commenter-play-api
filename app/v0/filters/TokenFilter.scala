package v0.filters

import play.api.mvc._
import play.api.Configuration

import scalikejdbc._

import util.{Try, Success, Failure}

import scala.concurrent.duration._

import v0._
import models.entities.User

class TokenFilter(private val config: Configuration) {

  ConnectionPool.singleton(
    config.get[String]("db.url"),
    config.get[String]("db.username"),
    config.get[String]("db.password")
  )
  implicit val session = AutoSession

  val tokenExpirationDuration = 1.day

  def checkUserToken()(implicit request: Request[AnyContent]): Try[User] = {
    def mkUserEntity(rs: WrappedResultSet) = User(
      id = rs.get("id"),
      name = rs.get("name")
    )
  
    (request.headers.get("Authorization") match {
      case None => Failure(new NonExistTokenInHeaderException)
      case Some(token) => Success(token)
    }) flatMap {
      case token => Try {
        // 有効期限内のトークンを探し、auths, usersと紐づけてUserのエンティティを返す
        sql"""
          select u.id as id, u.name as name
            from tokens as t
              inner join auths as a
                on t.auth_id = a.id
              inner join users as u
                on a.id = u.auth_id
            where
              token = ${token}
              and created_at >= current_timestamp - interval ${tokenExpirationDuration.toMinutes} minute;
        """
          .map(rs => mkUserEntity(rs))
          .single.apply()
      }
    } flatMap {
      case Some(user) => Success(user)
      case None => Failure(new InvalidTokenException)
    }
  }

}

class TokenFilterException extends Exception
class NonExistTokenInHeaderException extends TokenFilterException
class InvalidTokenException extends TokenFilterException
