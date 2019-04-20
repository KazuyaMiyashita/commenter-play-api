package v0.filters

import play.api.mvc._
import scalikejdbc._

import util.{Try, Success, Failure}

import scala.concurrent.duration._

import v0._
import models.entities.User

object TokenFilter {

  val tokenExpirationDuration = 1.day

  private def mkUserEntity(rs: WrappedResultSet) = User(
    id = rs.get("id"),
    name = rs.get("name")
  )

  def checkUserToken()(implicit request: Request[AnyContent]): Try[User] = {
    (request.headers.get("Authorization") match {
      case None => Failure(new NonExistTokenException)
      case Some(token) => Success(token)
    }) flatMap {
      case token => Try {
        // 有効期限内のトークンを探し、auths, usersと紐づけてUserのエンティティを返す
        sql"""
          select t.token, t.auth_username, a.password
            from tokens as t inner join auths as a
            on t.auth_username = a.username
            where
              token = ${token}
              and created_at >= current_timestamp - interval ${tokenExpirationDuration.toMinutes} minute;
        """
        // usersまで紐づける
      }
    }
  
    ???
  }

}

class TokenFilterException extends Exception
class NonExistTokenException extends TokenFilterException
class InvalidTokenException extends TokenFilterException
