package repository.persistance.database

import config.AppConfig
import org.flywaydb.core.Flyway

trait MigrationConfig extends AppConfig {

  private val flyway = new Flyway()
  flyway.setDataSource(url, user, password)

  def migrate() = {
    flyway.migrate()
  }

  def reloadSchema() = {
    flyway.clean()
    flyway.migrate()
  }

  def cleanSchema() = {
    flyway.clean()
  }

}
