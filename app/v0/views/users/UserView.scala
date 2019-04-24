package v0.views.users

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._
import views.View
import models.entities.User

object UserView extends View {

  def showDetails(user: User): JsValue = Json.obj(
    "id" -> user.id,
    "name" -> user.name
  )

}
