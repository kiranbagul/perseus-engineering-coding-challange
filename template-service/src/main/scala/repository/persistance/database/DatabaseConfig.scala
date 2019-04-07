package repository.persistance.database

trait DatabaseConfig {
  val driver = slick.jdbc.MySQLProfile

  import driver.api._

  def db = Database.forConfig("db.sql")

  implicit val session: Session = db.createSession()
}
