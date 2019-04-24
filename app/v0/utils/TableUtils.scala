package v0.utils

object TableUtils {

  def handleMySQLError(error: Throwable): Throwable = {
    error match {
      case sqlError: java.sql.SQLException => {
        sqlError.getErrorCode match {
          case 1062 => new Throwable // Duplicate entry
          case 1452 => new Throwable // Cannot add or update a child row: a foreign key constraint fails
        }
      }
      case _ => error
    }
  }

}
