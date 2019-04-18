package v0.controllers

import javax.inject._
import play.api._
import play.api.mvc._

import v0.models.entities.User
import v0.models.forms.User

@Singleton
class UsersController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def get() = Action { implicit request: Request[AnyContent] =>
    Ok("v0.UsersController.get")
  }

}
