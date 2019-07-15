package v0.controllers.actions

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.{Future, ExecutionContext}

class TokenActionFilter @Inject()(implicit ec: ExecutionContext) extends ActionFilter[Request] {

  protected def filter[A](request: Request[A]): Future[Option[Result]] = {
    Future.successful(None)
  }
  // protected def filter[A](request: play.api.mvc.Request[A]): scala.concurrent.Future[Option[play.api.mvc.Result]] = ???
  
  // Members declared in play.api.mvc.ActionFunction
  protected def executionContext = ec
}


// class UserRequest[A](val username: Option[String], request: Request[A]) extends WrappedRequest[A](request)

// class UserAction @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
//     extends ActionBuilder[UserRequest, AnyContent]
//     with ActionTransformer[Request, UserRequest] {
//   def transform[A](request: Request[A]) = Future.successful {
//     new UserRequest(request.session.get("username"), request)
//   }
// }


// object Execution in package concurrent is deprecated (since 2.6.0): Please see https://www.playframework.com/documentation/2.6.x/Migration26#play.api.libs.concurrent.Execution-is-deprecatedscalac
