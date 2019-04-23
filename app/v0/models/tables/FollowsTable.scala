package v0.models.tables

import play.api.Configuration

import scalikejdbc._

import util.{Try, Success, Failure}

import v0.models.entities.User
import v0.models.forms.FollowForm

class FollowsTable(private val config: Configuration) {

  ConnectionPool.singleton(
    config.get[String]("db.url"),
    config.get[String]("db.username"),
    config.get[String]("db.password")
  )
  implicit val session = AutoSession

  import FollowsTable._

  def follow(mine: User, followForm: FollowForm): Try[Unit] = {
    val follower: String = mine.id
    val followee: String = followForm.followee
    
    if (follower == followee) Failure(new FollowMyselfException)
    else Try {
      sql"insert into follows (follower, followee) values (${follower}, ${followee})"
        .update.apply()
    }
  }

  def follower(user: User): Try[List[User]] = Try {
    sql"""
      select id, name from users as u
        inner join follows as f
          on f.follower = ${user.id}
          and f.followee = u.id;
    """
      .map(rs => toUserEntity(rs)).list.apply()
  }

  def followee(user: User): Try[List[User]] = Try {
    sql"""
      select id, name from users as u
        inner join follows as f
          on f.followee = ${user.id}
          and f.follower = u.id;
    """
      .map(rs => toUserEntity(rs)).list.apply()
  }

}

class FollowsTableException extends Exception
class FollowMyselfException extends FollowsTableException

object FollowsTable {
  
  def toUserEntity(rs: WrappedResultSet): User = User (
    id = rs.get("id"),
    name = rs.get("name")
  )

}
