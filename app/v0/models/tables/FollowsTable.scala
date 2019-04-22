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

  def follow(mine: User, followForm: FollowForm): Try[Unit] = Try {
    val follow_user_id: String = mine.id
    val followed_user_id: String = followForm.followedUserId
    sql"insert into follows (follow_user_id, followed_user_id) values (${follow_user_id}, ${followed_user_id})"
      .update.apply()
  }

}
