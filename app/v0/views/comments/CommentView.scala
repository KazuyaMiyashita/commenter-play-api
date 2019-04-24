package v0.views.comments

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._
import views.View
import models.entities.Comment
import models.forms.CommentForm

object CommentView extends View {

  def showComments(comments: Seq[Comment]): JsValue = {
    Json.toJson(
      comments map { comment =>
        Json.obj(
          "id" -> comment.id,
          "userId" -> comment.userId,
          "userName" -> comment.userName,
          "comment" -> comment.comment
        )
      }
    )
  }

}
