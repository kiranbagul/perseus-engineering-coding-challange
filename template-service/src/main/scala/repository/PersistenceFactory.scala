package repository

import com.typesafe.scalalogging.Logger
import config.AppConfig
import domain.Template
import repository.persistance.InMemoryTemplateRepositoryImpl
import repository.persistance.database.{MigrationConfig, TemplateRepositoryImpl}

import scala.concurrent.ExecutionContext
import scala.io.Source

object PersistenceFactory extends AppConfig with MigrationConfig {

  def log = Logger("PersistenceFactory")

  private var templates: Seq[Template] = Nil

  def init(): Unit = {
    val newsletter = Source.fromResource("templates/newsletter.txt").getLines().mkString
    val welcome = Source.fromResource("templates/welcome.txt").getLines().mkString
    templates = Seq(Template(1, "newsletter", newsletter), Template(2, "welcome", welcome))
  }

  def getRepository()(implicit ec: ExecutionContext): TemplateRepository = {
    log.info(s" Using ${if (mode) "in-memory" else "persistent"} datastore")
    init()
    mode match {
      case false =>
        reloadSchema()
        new TemplateRepositoryImpl(templates)
      case true =>
        new InMemoryTemplateRepositoryImpl(templates)
    }
  }
}