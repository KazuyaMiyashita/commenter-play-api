package v0.models.entities

import play.api.libs.json.Json

case class Auth(
  username: String,
  password: String
)
