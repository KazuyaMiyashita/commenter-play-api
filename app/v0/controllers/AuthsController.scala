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
import utils.ControllerUtils._


@Singleton
class AuthsController @Inject()(config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  val authsTable = new AuthsTable(config)

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.AuthsController.get")
  }

  def save() = Action { implicit request: Request[AnyContent] =>
    val saveView = new SaveView
    (bindFromRequest(AuthForm.form) match {
      case Right(form) => Right(authsTable.save(form))
      case Left(badForm) => Left(BadRequest(saveView.onFormError(badForm)))
    }) flatMap {
      case Success(_) => Right(Ok)
      case Failure(f: java.sql.SQLIntegrityConstraintViolationException) => Left(Conflict(saveView.onError(f)))
      case Failure(f) => Left(InternalServerError(saveView.onError(f)))
    } merge
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    val loginView = new LoginView
    (bindFromRequest(AuthForm.form) match {
      case Right(form) => Right(authsTable.login(form))
      case Left(badForm) => Left(BadRequest(loginView.onFormError(badForm)))
    }).flatMap {
      case Success(token) => Right(Ok(loginView.onOK(token)))
      case Failure(e: NonExistUserException) => Left(Forbidden(loginView.onError(e)))
      case Failure(e: InvalidPasswordException) => Left(Forbidden(loginView.onError(e)))
      case Failure(e) => Left(InternalServerError(loginView.onError(e)))
    } merge
  }

  def logout() = Action { implicit request: Request[AnyContent] =>
    val logoutView = new LogoutView
    (authsTable.logout()) match {
      case Success(_) => Ok
      case Failure(e) => InternalServerError(logoutView.onError(e))
    }
  }

}
