package v0.views.auths

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._
import views.View
import models.forms.CreateUserForm

object SaveView extends View {

  def onOK(token: String): JsValue = Json.obj(
    "token" -> token
  )

}
