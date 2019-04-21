package v0.views.auths

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._

class LogoutView(implicit messageProvider: MessagesProvider) {

  def onError(error: Throwable): JsValue = Json.obj(
    "error" -> error.toString
  )

}
