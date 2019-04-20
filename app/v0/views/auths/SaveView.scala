package v0.views.auths

import play.api.data.Form
import play.api.libs.json._

import v0._
import models.forms.AuthForm

object SaveView {

  def onOK(token: String): JsValue = Json.obj(
    "token" -> token
  )

  def onError(error: Throwable): JsValue = Json.obj(
    "error" -> error.toString
  )

  def onFormError(badForm: Form[AuthForm]): JsValue = {
    // badForm.errorsAsJson
    Json.arr(badForm.errors.map(err => Json.obj(
      "key" -> err.key,
      "messages" -> err.messages
    )))
  }

}
