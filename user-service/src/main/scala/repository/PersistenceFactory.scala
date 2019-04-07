package repository

import com.typesafe.scalalogging.Logger
import config.AppConfig
import domain.User
import io.circe.generic.auto._
import io.circe.parser._
import repository.persistance.InMemoryUserRepositoryImpl
import repository.persistance.database.{MigrationConfig, UserRepositoryImpl}

import scala.concurrent.ExecutionContext
import scala.io.Source


object PersistenceFactory extends AppConfig with MigrationConfig {
  def log = Logger("PersistenceFactory")

  private val usersJson = Source.fromResource("users.json").getLines().mkString

  def getRepository()(implicit ec: ExecutionContext): UserRepository = {
    log.info(s" Using ${if (mode) "in-memory" else "persistent"} datastore")
    val mayBeUsers = decode[Seq[domain.User]](usersJson)
    val users: Seq[User] = mayBeUsers.getOrElse(Seq.empty[User])
    mode match {
      case false =>
        reloadSchema()
        new UserRepositoryImpl(users)
      case true =>
        new InMemoryUserRepositoryImpl(users)
    }
  }
}