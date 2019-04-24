package v0.utils

import com.mysql.cj.exceptions.MysqlErrorNumbers._

object TableUtils {

  def handleMySQLError(error: Throwable): Throwable = {
    error match {
      case sqlError: java.sql.SQLException => {
        sqlError.getErrorCode match {
          case ER_DUP_ENTRY => new Throwable
          case ER_NO_REFERENCED_ROW_2 => new Throwable
        }
      }
      case _ => error
    }
  }

}
