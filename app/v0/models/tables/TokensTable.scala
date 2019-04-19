// package v0.models.tables

// import play.api.Configuration

// import scalikejdbc._
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

// import util.Try

// // import v0.models.forms.AuthForm

// class TokensTable(private val config: Configuration) {

//   ConnectionPool.singleton(
//     config.get[String]("db.url"),
//     config.get[String]("db.username"),
//     config.get[String]("db.password")
//   )
//   implicit val session = AutoSession

//   val bcrypt = new BCryptPasswordEncoder()
//   def createHash(password: String): String = bcrypt.encode(password)

//   def save(data: AuthForm): Try[Int] = Try {
//     val username = data.username
//     val hashedPassword = createHash(data.password)
//     sql"insert into auths (username, password) values (${data.username}, ${hashedPassword})".update.apply()
//   }

// }
