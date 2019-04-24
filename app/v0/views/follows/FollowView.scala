package v0.views.follows

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._
import views.View
import models.entities.User
import models.forms.FollowForm

object FollowView extends View {

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

}
