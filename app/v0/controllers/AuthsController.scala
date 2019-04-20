package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.i18n.I18nSupport

import util.{Try, Success, Failure}

import v0._

import models.entities.Auth
import models.forms.AuthForm
import models.tables._
import views.auths._


@Singleton
class AuthsController @Inject()(config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport{

  val authsTable = new AuthsTable(config)

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.AuthsController.get")
  }

  def save() = Action { implicit request: Request[AnyContent] =>
    val saveView = new SaveView
    AuthForm.form.bindFromRequest().fold(
      badForm => BadRequest(saveView.onFormError(badForm)),
      form => (authsTable.save(form): Try[Unit]) match {
        case Success(_) => Ok
        case Failure(f: java.sql.SQLIntegrityConstraintViolationException) =>
          Conflict(saveView.onError(f))
        case Failure(f) => InternalServerError(saveView.onError(f))
      }
    )
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    val loginView = new LoginView
    AuthForm.form.bindFromRequest().fold(
      badForm => BadRequest(loginView.onFormError(badForm)),
      form => (authsTable.login(form): Try[String]) match {
        case Success(token) => Ok(loginView.onOK(token))
        case Failure(e: NonExistUserException) => Forbidden(loginView.onError(e))
        case Failure(e: InvalidPasswordException) => Forbidden(loginView.onError(e))
        case Failure(e) => InternalServerError(loginView.onError(e))
      }
    )
  }

}
