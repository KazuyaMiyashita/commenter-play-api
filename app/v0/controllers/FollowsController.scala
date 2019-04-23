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
import models.tables.FollowsTable
import views.follows.FollowView
import utils.ControllerUtils._

@Singleton
class FollowsController @Inject()(config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  val tokenFilter = new TokenFilter(config)
  val followTable = new FollowsTable(config)

  def follow() = Action { implicit request: Request[AnyContent] => 
    val view = new FollowView
    (tokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized(view.onError(e)))
      case Failure(e) => Left(InternalServerError(view.onError(e)))
    }) flatMap {
      user => (bindFromRequest(FollowForm.form) match {
        case Right(form) => Right(followTable.follow(user, form))
        case Left(badForm) => Left(BadRequest(view.onFormError(badForm)))
      }) flatMap {
        case Success(_) => Right(Ok)
        case Failure(f) => Left(InternalServerError(view.onError(f)))
      }
    } merge
  }

  def getFollowers() = Action { implicit request: Request[AnyContent] =>
    val view = new FollowView
    (tokenFilter.checkUserToken() match {
      case Success(user) => Right(followTable.follower(user))
      case Failure(e: TokenFilterException) => Left(Unauthorized(view.onError(e)))
      case Failure(e) => Left(InternalServerError(view.onError(e)))
    }) flatMap {
      case Success(users) => Right(Ok(view.showUsers(users)))
      case Failure(f) => Left(InternalServerError(view.onError(f)))
    } merge
  }

  def getFollowees() = Action { implicit request: Request[AnyContent] =>
    val view = new FollowView
    (tokenFilter.checkUserToken() match {
      case Success(user) => Right(followTable.followee(user))
      case Failure(e: TokenFilterException) => Left(Unauthorized(view.onError(e)))
      case Failure(e) => Left(InternalServerError(view.onError(e)))
    }) flatMap {
      case Success(users) => Right(Ok(view.showUsers(users)))
      case Failure(f) => Left(InternalServerError(view.onError(f)))
    } merge
  }

}
