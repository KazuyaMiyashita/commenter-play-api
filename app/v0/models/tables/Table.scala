package v0.models.tables

import com.mysql.cj.exceptions.MysqlErrorNumbers
import util.{Try, Success, Failure}

trait MySQLException extends Exception {
  val e: java.sql.SQLException
}

class ER_DUP_ENTRY(val e: java.sql.SQLException) extends MySQLException
class ER_NO_REFERENCED_ROW_2(val e: java.sql.SQLException) extends MySQLException

class Table 

object Table {

  def handleMySQLError[U](pf: PartialFunction[Throwable, Try[U]]): PartialFunction[Throwable, Try[U]] = {
    case e: java.sql.SQLException => {
      e.getErrorCode match {
        case MysqlErrorNumbers.ER_DUP_ENTRY => Failure(new ER_DUP_ENTRY(e))
        case MysqlErrorNumbers.ER_NO_REFERENCED_ROW_2 => Failure(new ER_NO_REFERENCED_ROW_2(e))
        case _ => Failure(e)
      }
    } recoverWith(pf)
  }

}
