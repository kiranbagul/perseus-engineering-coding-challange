package repository

import domain.User

import scala.concurrent.Future

trait UserRepository {

  def getUser(userId: Long): Future[Option[User]]

  def all: Future[Seq[User]]

}
