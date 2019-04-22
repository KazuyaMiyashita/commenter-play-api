package v0.models.forms

import play.api.data.Form
import play.api.data.Forms._

case class FollowForm(
  followedUserId: String,
)

object FollowForm {

  val form: Form[FollowForm] = Form(
    mapping(
      "followedUserId" -> nonEmptyText
    )(FollowForm.apply)(FollowForm.unapply)
  )

}
