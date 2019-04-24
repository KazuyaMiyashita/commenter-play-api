package v0.views.auths

import play.api.data.Form
import play.api.libs.json._
import play.api.i18n.MessagesProvider

import v0._

object LogoutView {

  def onError(error: Throwable)(implicit messageProvider: MessagesProvider): JsValue = Json.obj(
    "error" -> error.toString
  )

}
