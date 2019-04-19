package v0.models.entities

import play.api.libs.json.Json

class DisplayErrorAsJson(
  private val error: Throwable,
) {

  val toJson = Json.obj(
    "error" -> error.toString
  )
    
}
