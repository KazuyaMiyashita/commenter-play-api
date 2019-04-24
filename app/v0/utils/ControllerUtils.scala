package v0.controllers.utils

import play.api.mvc._
import play.api.data._

object ControllerUtils {

  def bindFromRequest[T](form: Form[T])(implicit request: Request[_])
    : Either[Form[T], T] = {

    val binded = form.bindFromRequest()
    binded.value match {
      case Some(v) if form.errors.isEmpty => Right(v)
      case _ => Left(binded)
    }
  }

}
