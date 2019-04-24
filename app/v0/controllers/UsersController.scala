package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.i18n.I18nSupport

import util.{Try, Success, Failure}

import v0._
import models.entities.User
import views.users.UserView
import filters._


@Singleton
class UsersController @Inject()(implicit config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.UsersController.get")
  }

  def getMe() = Action { implicit request: Request[AnyContent] =>
    (TokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized(UserView.onError(e)))
      case Failure(e) => Left(InternalServerError(UserView.onError(e)))
    }) flatMap {
      user => Right(Ok(UserView.showDetails(user)))
    } merge
  }

}
