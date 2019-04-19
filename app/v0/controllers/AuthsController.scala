package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.i18n.I18nSupport

import util.{Try, Success, Failure}

import v0.models.entities.Auth
import v0.models.forms.AuthForm
import v0.models.tables._

import v0.models.entities.DisplayErrorAsJson

@Singleton
class AuthsController @Inject()(config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  val authsTable = new AuthsTable(config)

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.AuthsController.get")
  }

  def save() = Action { implicit request: Request[AnyContent] =>
    AuthForm.form.bindFromRequest().fold(
      badForm => BadRequest(badForm.errorsAsJson),
      form => (authsTable.save(form): Try[Unit]) match {
        case Success(_) => Ok
        case Failure(f: java.sql.SQLIntegrityConstraintViolationException) =>
          Conflict(new DisplayErrorAsJson(f).toJson)
        case Failure(f) => InternalServerError(new DisplayErrorAsJson(f).toJson)
      }
    )
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    AuthForm.form.bindFromRequest().fold(
      badForm => BadRequest(badForm.errorsAsJson),
      form => (authsTable.login(form): Try[String]) match {
        case Success(token) => Ok(token)
        case Failure(e: NonExistUserException) => Forbidden(new DisplayErrorAsJson(e).toJson)
        case Failure(e: InvalidPasswordException) => Forbidden(new DisplayErrorAsJson(e).toJson)
        case Failure(e) => InternalServerError(new DisplayErrorAsJson(e).toJson)
      }
    )
  }

}
