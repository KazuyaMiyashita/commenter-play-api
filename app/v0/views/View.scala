package v0.views

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._

trait View {

  def onError(error: Throwable): JsValue = Json.obj(
    "error" -> error.toString
  )

  def onFormError(badForm: Form[_])(implicit messageProvider: MessagesProvider): JsValue = {
    Json.obj(
      "error" -> badForm.errorsAsJson
    )
  }

}
