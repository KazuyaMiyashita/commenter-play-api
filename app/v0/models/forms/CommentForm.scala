package v0.models.forms

import play.api.data.Form
import play.api.data.Forms._

case class CommentForm(
  comment: String,
)

object CommentForm {

  val form: Form[CommentForm] = Form(
    mapping(
      "comment" -> nonEmptyText(maxLength = 255)
    )(CommentForm.apply)(CommentForm.unapply)
  )

}
