package v0.views.follows

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._
import models.entities.User
import models.forms.FollowForm

object FollowView {

  def showUsers(users: Seq[User]): JsValue = {
    Json.arr(
      users map { user =>
        Json.obj(
          "id" -> user.id,
          "name" -> user.name,
        )
      }
    )
  }

  def onFormError(badForm: Form[FollowForm])(implicit messageProvider: MessagesProvider): JsValue = {
    Json.obj(
      "error" -> badForm.errorsAsJson
    )
  }

  def onError(error: Throwable): JsValue = Json.obj(
    "error" -> error.toString
  )

}
