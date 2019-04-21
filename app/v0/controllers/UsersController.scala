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
class UsersController @Inject()(config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  val tokenFilter = new TokenFilter(config)

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.UsersController.get")
  }

  def getMyDetails() = Action { implicit request: Request[AnyContent] =>
    val userView = new UserView
    (tokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized)
      case Failure(e) => Left(InternalServerError(userView.onError(e)))
    }) flatMap {
      user => Right(Ok(userView.showDetails(user)))
    } merge
  }

}
