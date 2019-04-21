package v0.views.auths

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._
import models.forms.AuthLoginForm

class LoginView(implicit messageProvider: MessagesProvider) {

  def onOK(token: String): JsValue = Json.obj(
    "token" -> token
  )

  def onError(error: Throwable): JsValue = Json.obj(
    "error" -> error.toString
  )

  def onFormError(badForm: Form[AuthLoginForm]): JsValue = {
    Json.obj(
      "error" -> badForm.errorsAsJson
    )
  }

}
