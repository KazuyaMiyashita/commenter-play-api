package v0.controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class UsersControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "v0/UsersController GET" should {

    // "render the index page from a new instance of controller" in {
    // // 引数にConfiguration.emptyを渡すことができない
    //   val controller = new UsersController(Configuration.empty, stubControllerComponents())
    //   val home = controller.get().apply(FakeRequest(GET, "/v0/users"))

    //   status(home) mustBe OK
    //   contentType(home) mustBe Some("text/plain")
    //   contentAsString(home) must include ("v0.UsersController.get")
    // }

    "render the index page from the application" in {
      val controller = inject[UsersController]
      val home = controller.get().apply(FakeRequest(GET, "/v0/users"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")
      contentAsString(home) must include ("v0.UsersController.get")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/v0/users")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")
      contentAsString(home) must include ("v0.UsersController.get")
    }
  }
}
