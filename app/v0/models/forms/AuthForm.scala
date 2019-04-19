package v0.models.forms

case class AuthForm(
  username: String,
  password: String
)

object AuthForm {

  import play.api.data.Form
  import play.api.data.Forms._

  val form: Form[AuthForm] = Form(
    mapping(
      "username" -> email,
      "password" -> nonEmptyText(minLength = 8)
    )(AuthForm.apply)(AuthForm.unapply)
  )

}
