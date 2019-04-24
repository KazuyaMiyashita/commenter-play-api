package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.i18n.I18nSupport

import util.{Try, Success, Failure}

import v0._

import models.entities.Auth
import models.forms.{CreateUserForm, AuthLoginForm}
import models.tables._
import views.auths.{SaveView, LoginView, LogoutView}
import utils.ControllerUtils._


@Singleton
class AuthsController @Inject()(implicit config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.AuthsController.get")
  }

  def save() = Action { implicit request: Request[AnyContent] =>
    (bindFromRequest(CreateUserForm.form) match {
      case Right(form) => Right(AuthsTable.save(form))
      case Left(badForm) => Left(BadRequest(SaveView.onFormError(badForm)))
    }) flatMap {
      case Success(_) => Right(Ok)
      case Failure(f: java.sql.SQLIntegrityConstraintViolationException) => Left(Conflict(SaveView.onError(f)))
      case Failure(f) => Left(InternalServerError(SaveView.onError(f)))
    } merge
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    (bindFromRequest(AuthLoginForm.form) match {
      case Right(form) => Right(AuthsTable.login(form))
      case Left(badForm) => Left(BadRequest(LoginView.onFormError(badForm)))
    }).flatMap {
      case Success(token) => Right(Ok(LoginView.onOK(token)))
      case Failure(e: NonExistUserException) => Left(Forbidden(LoginView.onError(e)))
      case Failure(e: InvalidPasswordException) => Left(Forbidden(LoginView.onError(e)))
      case Failure(e) => Left(InternalServerError(LoginView.onError(e)))
    } merge
  }

  def logout() = Action { implicit request: Request[AnyContent] =>
    (AuthsTable.logout()) match {
      case Success(_) => Ok
      case Failure(e) => InternalServerError(LogoutView.onError(e))
    }
  }

}
