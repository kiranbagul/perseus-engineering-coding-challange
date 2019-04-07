package repository.persistance

import repository.UserRepository

import scala.concurrent.Future

class InMemoryUserRepositoryImpl(collection: Seq[domain.User] = Seq.empty) extends UserRepository {

  def getUser(userId: Long): Future[Option[domain.User]] = {
    val maybeUser = collection.find(u => {
      u.id.equals(userId)
    })
    Future.successful(maybeUser)
  }

  def all: Future[Seq[domain.User]] = {
    Future.successful(collection)
  }

}
