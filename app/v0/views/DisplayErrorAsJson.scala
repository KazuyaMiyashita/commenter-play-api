package v0.views

import play.api.libs.json.Json

class DisplayErrorAsJson(
  private val error: Throwable,
) {

  val toJson = Json.obj(
    "error" -> error.toString
  )
    
}
