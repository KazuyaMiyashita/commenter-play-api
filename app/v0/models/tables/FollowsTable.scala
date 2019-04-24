package v0.models.tables

import play.api.Configuration

import scalikejdbc._

import util.{Try, Success, Failure}

import v0.models.entities.User
import v0.models.forms.FollowForm


trait FollowsTableException extends Exception
class FollowMyselfException extends FollowsTableException
class FollowDuplicateException extends FollowsTableException
class FollowNonExistUserException extends FollowsTableException

object FollowsTable {

  implicit val session = AutoSession

  def follow(mine: User, followForm: FollowForm)(implicit config: Configuration): Try[Unit] = {
    Table.getConnectionPool(config)
    val follower: String = mine.id
    val followee: String = followForm.followee
    
    if (follower == followee) Failure(new FollowMyselfException)
    else Try {
      val _ = sql"insert into follows (follower, followee) values (${follower}, ${followee})"
        .update.apply()
    } recoverWith(Table.handleMySQLError) {
      case e: ER_NO_REFERENCED_ROW_2 => Failure(new FollowNonExistUserException)
      case e: ER_DUP_ENTRY => Failure(new FollowDuplicateException)
    }
  }

  def follower(user: User)(implicit config: Configuration): Try[List[User]] = Try {
    Table.getConnectionPool(config)
    sql"""
      select id, name from users as u
        inner join follows as f
          on f.follower = ${user.id}
          and f.followee = u.id;
    """
      .map(rs => toUserEntity(rs)).list.apply()
  }

  def followee(user: User)(implicit config: Configuration): Try[List[User]] = Try {
    Table.getConnectionPool(config)
    sql"""
      select id, name from users as u
        inner join follows as f
          on f.followee = ${user.id}
          and f.follower = u.id;
    """
      .map(rs => toUserEntity(rs)).list.apply()
  }
  
  private def toUserEntity(rs: WrappedResultSet): User = User (
    id = rs.get("id"),
    name = rs.get("name")
  )

}
