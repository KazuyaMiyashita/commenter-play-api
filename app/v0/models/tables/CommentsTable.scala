package v0.models.tables

import play.api.Configuration

import scalikejdbc._

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import de.huxhorn.sulky.ulid.ULID;

import util.{Try, Success, Failure}

import v0.models.entities.{User, Comment}
import v0.models.forms.CommentForm

class CommentsTable(private val config: Configuration) {

  ConnectionPool.singleton(
    config.get[String]("db.url"),
    config.get[String]("db.username"),
    config.get[String]("db.password")
  )
  implicit val session = AutoSession

  import CommentsTable._

  def get(user: User): Try[List[Comment]] = Try {
    val follower = user.id
    val comments: List[Comment] =
      sql"""
        select c.id as id, c.user_id as user_id, c.comment as comment, u.name as user_name
          from comments as c
            inner join users as u
              on c.user_id = u.id
            where exists (
              select * from follows
                where follower = ${follower}
                and c.user_id = follows.followee
            )
          order by created_at desc;
      """
        .map(rs => toCommentEntity(rs)).list.apply()
    comments
  }

  def getAll(): Try[List[Comment]] = Try {
    val comments: List[Comment] =
      sql"""
      select c.id as id, c.user_id as user_id, c.comment as comment, u.name as user_name
        from comments as c
          inner join users as u
            on c.user_id = u.id
        order by created_at desc;
    """
        .map(rs => toCommentEntity(rs)).list.apply()
    comments
  }

  def comment(form: CommentForm, user: User): Try[Unit] = Try {
    val id: String = createULID()
    val user_id: String = user.id
    val comment: String = form.comment
    sql"insert into comments (id, user_id, comment, created_at) values (${id}, ${user_id}, ${comment}, current_timestamp)"
      .update.apply()
  }

}

class CommentsTableException extends Exception

object CommentsTable {

  def createULID(): String = (new ULID).nextULID

  private def toCommentEntity(rs: WrappedResultSet): Comment = Comment(
    id = rs.get("id"),
    userId = rs.get("user_id"),
    userName = rs.get("user_name"),
    comment = rs.get("comment")
  )

}
