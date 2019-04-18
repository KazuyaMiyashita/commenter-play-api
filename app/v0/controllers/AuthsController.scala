package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.i18n.I18nSupport

import util.{Try, Success, Failure}

import v0.models.entities.Auth
import v0.models.forms.AuthForm
import v0.models.tables.AuthsTable

@Singleton
class AuthsController @Inject()(cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.AuthsController.get")
  }

  def save() = Action { implicit request: Request[AnyContent] =>
    AuthForm.form.bindFromRequest().fold(
      badForm => BadRequest(badForm.errorsAsJson),
      form => (AuthsTable.save(form): Try[Int]) match {
        case Success(s) => Ok(s.toString)
        case Failure(f) => InternalServerError(f.toString)
      }
    )
  }

}
