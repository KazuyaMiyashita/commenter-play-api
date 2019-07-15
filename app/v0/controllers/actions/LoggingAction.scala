package v0.controllers.actions

import javax.inject.Inject
import play.api.mvc._
import play.api.Logging
import scala.concurrent.{Future, ExecutionContext}

class LoggingAction @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser)
    with Logging {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    logger.info("Calling action")
    println("hoge")
    block(request)
  }
}
