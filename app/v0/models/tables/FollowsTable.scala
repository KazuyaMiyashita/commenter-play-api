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

  def follow(mine: User, followForm: FollowForm): Try[Unit] = Try {
    val follow_user_id: String = mine.id
    val followed_user_id: String = followForm.followedUserId
    sql"insert into follows (follow_user_id, followed_user_id) values (${follow_user_id}, ${followed_user_id})"
      .update.apply()
  }

  def follower(user: User): Try[List[User]] = Try {
    sql"""
      select id, name from users as u
        inner join follows as f
          on f.follow_user_id = ${user.id}
          and f.followed_user_id = u.id;
    """
      .map(rs => toUserEntity(rs)).list.apply()
  }

  def followee(user: User): Try[List[User]] = Try {
    sql"""
      select id, name from users as u
        inner join follows as f
          on f.followed_user_id = ${user.id}
          and f.follow_user_id = u.id;
    """
      .map(rs => toUserEntity(rs)).list.apply()
  }

}

object FollowsTable {
  
  def toUserEntity(rs: WrappedResultSet): User = User (
    id = rs.get("id"),
    name = rs.get("name")
  )

}
