package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.i18n.I18nSupport

import util.{Try, Success, Failure}

import v0._

import models.entities.{User, Comment}
import models.forms.CommentForm
import models.tables._
import views.comments._
import utils.ControllerUtils._
import filters._
import v0.controllers.actions.TokenActionFilter
import v0.controllers.actions.LoggingAction

@Singleton
class CommentsController @Inject()
  (tokenActionFilter: TokenActionFilter, loggingAction: LoggingAction)
  (implicit config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  def get() = tokenActionFilter { implicit request: Request[AnyContent] =>
    (TokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized(CommentView.onError(e)))
      case Failure(e) => Left(InternalServerError(CommentView.onError(e)))
    }) flatMap { user: User =>
      CommentsTable.get(user) match {
        case Success(comments) => Right(Ok(CommentView.showComments(comments)))
        case Failure(f) => Left(InternalServerError(CommentView.onError(f)))
      }
    } merge

  }
  
  def getAll() = Action { implicit request: Request[AnyContent] =>
    (TokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized(CommentView.onError(e)))
      case Failure(e) => Left(InternalServerError(CommentView.onError(e)))
    }) flatMap {
      _ => Right(CommentsTable.getAll())
    } flatMap {
      case Success(comments) => Right(Ok(CommentView.showComments(comments)))
      case Failure(f) => Left(InternalServerError(CommentView.onError(f)))
    } merge
  }

  def comment() = Action { implicit request: Request[AnyContent] =>
    (TokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized(CommentView.onError(e)))
      case Failure(e) => Left(InternalServerError(CommentView.onError(e)))
    }) flatMap {
      user: User => {
        (bindFromRequest(CommentForm.form) match {
          case Right(form) => Right(CommentsTable.comment(form, user))
          case Left(badForm) => Left(BadRequest(CommentView.onFormError(badForm)))
        }) flatMap {
          case Success(_) => Right(Ok)
          case Failure(f) => Left(InternalServerError(CommentView.onError(f)))
        }
      }
    } merge
  }

}
