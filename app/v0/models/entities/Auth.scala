package v0.models.entities

import play.api.libs.json.Json

case class Auth(
  id: String,
  email: String,
  hashedPassword: String
)
