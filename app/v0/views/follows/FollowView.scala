package v0.views.follows

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._
import models.forms.FollowForm

class FollowView(implicit messageProvider: MessagesProvider) {

  def onFormError(badForm: Form[FollowForm]): JsValue = {
    Json.obj(
      "error" -> badForm.errorsAsJson
    )
  }

  def onError(error: Throwable): JsValue = Json.obj(
    "error" -> error.toString
  )

}
