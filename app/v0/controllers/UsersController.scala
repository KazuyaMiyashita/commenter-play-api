package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import util.{Try, Success, Failure}

import v0._
import models.entities.User
import filters._


@Singleton
class UsersController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.UsersController.get")
  }

  def getMyDetails() = Action { implicit request: Request[AnyContent] =>
    (TokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized)
      case Failure(e) => Left(InternalServerError)
    }) flatMap {
      ???
    }

    ???
  }

}
