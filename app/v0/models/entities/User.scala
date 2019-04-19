package v0.models.entities

import play.api.libs.json.Json

case class User(
  id: UserId,
  info: UserInfo,
  privacies: UserPrivacies
)

case class UserId(
  value: Long
)

case class UserInfo(
  displayName: String
)

case class UserPrivacies(
  username: String,
  password: String
)
