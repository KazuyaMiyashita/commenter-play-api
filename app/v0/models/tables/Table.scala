package v0.models.tables

import play.api.Configuration
import scalikejdbc._
import com.mysql.cj.exceptions.MysqlErrorNumbers
import util.{Try, Success, Failure}

trait MySQLException extends Exception {
  val e: java.sql.SQLException
}

class ER_DUP_ENTRY(val e: java.sql.SQLException) extends MySQLException
class ER_NO_REFERENCED_ROW_2(val e: java.sql.SQLException) extends MySQLException
class ER_CHECK_CONSTRAINT_VIOLATED(val e: java.sql.SQLException) extends MySQLException

object Table {

  def handleMySQLError[U](pf: PartialFunction[Throwable, Try[U]]): PartialFunction[Throwable, Try[U]] = {
    case e: java.sql.SQLException => {
      println("e.getErrorCode: %d", e.getErrorCode)
      e.getErrorCode match {
        case MysqlErrorNumbers.ER_DUP_ENTRY => Failure(new ER_DUP_ENTRY(e))
        case MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2 => Failure(new ER_NO_REFERENCED_ROW_2(e))
        case 3819 => Failure(new ER_CHECK_CONSTRAINT_VIOLATED(e)) // TODO
        case _ => Failure(e)
      }
    } recoverWith(pf)
  }

  def getConnectionPool(config: Configuration) {
    ConnectionPool.singleton(
      config.get[String]("db.url"),
      config.get[String]("db.username"),
      config.get[String]("db.password")
    )
  }

}
