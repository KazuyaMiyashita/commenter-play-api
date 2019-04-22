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


@Singleton
class CommentsController @Inject()(config: Configuration, cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  val tokenFilter = new TokenFilter(config)
  val commentsTable = new CommentsTable(config)

  def get() = Action { implicit request: Request[AnyContent] =>
    val view = new CommentView
    (tokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized(view.onError(e)))
      case Failure(e) => Left(InternalServerError(view.onError(e)))
    }) flatMap { user: User =>
      commentsTable.get(user) match {
        case Success(comments) => Right(Ok(view.showComments(comments)))
        case Failure(f) => Left(InternalServerError(view.onError(f)))
      }
    } merge

  }
  
  def getAll() = Action { implicit request: Request[AnyContent] =>
    val view = new CommentView
    (tokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized(view.onError(e)))
      case Failure(e) => Left(InternalServerError(view.onError(e)))
    }) flatMap {
      _ => Right(commentsTable.getAll())
    } flatMap {
      case Success(comments) => Right(Ok(view.showComments(comments)))
      case Failure(f) => Left(InternalServerError(view.onError(f)))
    } merge
  }

  def comment() = Action { implicit request: Request[AnyContent] =>
    val view = new CommentView
    (tokenFilter.checkUserToken() match {
      case Success(user) => Right(user)
      case Failure(e: TokenFilterException) => Left(Unauthorized(view.onError(e)))
      case Failure(e) => Left(InternalServerError(view.onError(e)))
    }) flatMap {
      user: User => {
        (bindFromRequest(CommentForm.form) match {
          case Right(form) => Right(commentsTable.comment(form, user))
          case Left(badForm) => Left(BadRequest(view.onFormError(badForm)))
        }) flatMap {
          case Success(_) => Right(Ok)
          case Failure(f) => Left(InternalServerError(view.onError(f)))
        }
      }
    } merge
  }

}
