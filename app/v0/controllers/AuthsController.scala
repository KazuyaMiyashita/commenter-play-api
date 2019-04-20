package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import util.{Try, Success, Failure}

import v0._

import models.entities.Auth
import models.forms.AuthForm
import models.tables._
import views.auths._


@Singleton
class AuthsController @Inject()(config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc){

  val authsTable = new AuthsTable(config)

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.AuthsController.get")
  }

  def save() = Action { implicit request: Request[AnyContent] =>
    AuthForm.form.bindFromRequest().fold(
      badForm => BadRequest(SaveView.onFormError(badForm)),
      form => (authsTable.save(form): Try[Unit]) match {
        case Success(_) => Ok
        case Failure(f: java.sql.SQLIntegrityConstraintViolationException) =>
          Conflict(SaveView.onError(f))
        case Failure(f) => InternalServerError(SaveView.onError(f))
      }
    )
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    AuthForm.form.bindFromRequest().fold(
      badForm => BadRequest(SaveView.onFormError(badForm)),
      form => (authsTable.login(form): Try[String]) match {
        case Success(token) => Ok(LoginView.onOK(token))
        case Failure(e: NonExistUserException) => Forbidden(LoginView.onError(e))
        case Failure(e: InvalidPasswordException) => Forbidden(LoginView.onError(e))
        case Failure(e) => InternalServerError(LoginView.onError(e))
      }
    )
  }

}
