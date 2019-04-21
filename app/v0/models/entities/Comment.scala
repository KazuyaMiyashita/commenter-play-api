package v0.models.entities

import play.api.libs.json.Json

case class Comment(
  id: String,
  userId: String,
  comment: String
)
