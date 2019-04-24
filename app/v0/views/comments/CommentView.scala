package v0.views.comments

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._
import models.entities.Comment
import models.forms.CommentForm

object CommentView {

  def showComments(comments: Seq[Comment]): JsValue = {
    Json.arr(
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

  def onError(error: Throwable): JsValue = Json.obj(
    "error" -> error.toString
  )

  def onFormError(badForm: Form[CommentForm])(implicit messageProvider: MessagesProvider): JsValue = {
    Json.obj(
      "error" -> badForm.errorsAsJson
    )
  }

}
