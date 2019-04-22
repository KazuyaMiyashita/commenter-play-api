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
    val comments: List[Comment] =
      // sql"""
      //   select id, user_id, comment from comments

      //     where user_id in 
      //     order by created_at desc
      // """
      //   .map(rs => toCommentEntity(rs)).list.apply()
    comments

    ???
  }

  def getAll(): Try[List[Comment]] = Try {
    val comments: List[Comment] =
      sql"select id, user_id, comment from comments order by created_at desc"
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
    comment = rs.get("comment")
  )

}
