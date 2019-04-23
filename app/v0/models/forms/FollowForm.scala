package v0.models.forms

import play.api.data.Form
import play.api.data.Forms._

case class FollowForm(
  followee: String,
)

object FollowForm {

  val form: Form[FollowForm] = Form(
    mapping(
      "followee" -> nonEmptyText
    )(FollowForm.apply)(FollowForm.unapply)
  )

}
