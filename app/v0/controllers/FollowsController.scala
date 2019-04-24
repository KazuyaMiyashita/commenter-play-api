package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.i18n.I18nSupport

import util.{Try, Success, Failure}

import v0._
import filters._
import models.entities.User
import models.forms.FollowForm
import models.tables._
import views.follows.FollowView
import utils.ControllerUtils._

@Singleton
class FollowsController @Inject()(implicit config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  def follow() = Action { implicit request: Request[AnyContent] => 
    // val view = new FollowView
    (TokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized(FollowView.onError(e)))
      case Failure(e) => Left(InternalServerError(FollowView.onError(e)))
    }) flatMap {
      user => (bindFromRequest(FollowForm.form) match {
        case Right(form) => Right(FollowsTable.follow(user, form))
        case Left(badForm) => Left(BadRequest(FollowView.onFormError(badForm)))
      }) flatMap {
        case Success(_) => Right(Ok)
        case Failure(f: FollowMyselfException) => Left(BadRequest(FollowView.onError(f)))
        case Failure(f: FollowNonExistUserException) => Left(NotFound(FollowView.onError(f)))
        case Failure(f: FollowDuplicateException) => Left(Conflict(FollowView.onError(f)))
        case Failure(f) => Left(InternalServerError(FollowView.onError(f)))
      }
    } merge
  }

  def getFollowers() = Action { implicit request: Request[AnyContent] =>
    // val view = new FollowView
    (TokenFilter.checkUserToken() match {
      case Success(user) => Right(FollowsTable.follower(user))
      case Failure(e: TokenFilterException) => Left(Unauthorized(FollowView.onError(e)))
      case Failure(e) => Left(InternalServerError(FollowView.onError(e)))
    }) flatMap {
      case Success(users) => Right(Ok(FollowView.showUsers(users)))
      case Failure(f) => Left(InternalServerError(FollowView.onError(f)))
    } merge
  }

  def getFollowees() = Action { implicit request: Request[AnyContent] =>
    // val view = new FollowView
    (TokenFilter.checkUserToken() match {
      case Success(user) => Right(FollowsTable.followee(user))
      case Failure(e: TokenFilterException) => Left(Unauthorized(FollowView.onError(e)))
      case Failure(e) => Left(InternalServerError(FollowView.onError(e)))
    }) flatMap {
      case Success(users) => Right(Ok(FollowView.showUsers(users)))
      case Failure(f) => Left(InternalServerError(FollowView.onError(f)))
    } merge
  }

}
