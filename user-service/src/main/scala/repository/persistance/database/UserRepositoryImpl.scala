package repository.persistance.database

import domain.User
import repository.UserRepository
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

import scala.concurrent.Await
import scala.concurrent.duration._

class UserRepositoryImpl(collection: Seq[domain.User] = Seq.empty) extends BaseDao with UserRepository {

  val usersTable = TableQuery[UsersTable]

  Await.result(usersTable ++= collection, 10.seconds)

  def findAll: Future[Seq[User]] = usersTable.result

  def findById(userId: Long): Future[Option[User]] = usersTable.filter(_.id === userId).result.headOption

  override def getUser(userId: Long): Future[Option[User]] = findById(userId)

  override def all: Future[Seq[User]] = findAll

}