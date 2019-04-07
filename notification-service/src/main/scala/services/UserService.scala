package services

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import domain.User
import utils.UserServiceHTTPClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait UserService {
  def getUser(id: Long): Future[User]
  def getUsers: Future[Seq[User]]
}

class UserServiceImpl(userServiceHTTPClient: UserServiceHTTPClient) extends UserService {

  def log = Logger(classOf[UserService])

  private lazy val userServiceUrl = {
    val config = ConfigFactory.load()
    config.getString("userServiceUrl")
  }

  def getUser(id: Long): Future[User] = userServiceHTTPClient.getUser(userServiceUrl + "/" + id)

  def getUsers: Future[Seq[User]] = {
    val futureUsers = userServiceHTTPClient.getUsers(userServiceUrl)
    futureUsers recoverWith {
      case ex =>
        log.error("Error in retrieving users : ", ex)
        Future.failed(throw new RuntimeException(ex))
    }
  }

}