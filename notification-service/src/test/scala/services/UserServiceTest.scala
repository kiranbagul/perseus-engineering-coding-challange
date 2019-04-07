package services

import domain.User
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatest.{Matchers, WordSpec}
import utils.UserServiceHTTPClient

import scala.concurrent.Await
import scala.concurrent.Future.successful
import scala.concurrent.duration.Duration

class UserServiceTest extends WordSpec with Matchers with MockFactory {

  "UserService" should {
    "retrieve user by id " in {
      val client = mock[UserServiceHTTPClient]
      val userService = new UserServiceImpl(client)
      val user = new User(1, "", ",", "", "", true)
      (client.getUser _).expects(s"USER_SERVICE_BASE_URL/1").returns(successful(user))
      val eventualResponse = userService.getUser(1)
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(user)
      }
    }

    "retrieve all users" in {
      val client = mock[UserServiceHTTPClient]
      val userService = new UserServiceImpl(client)
      val user1 = new User(1, "", ",", "", "", true)
      val user2 = new User(2, "", ",", "", "", true)
      (client.getUsers _).expects(s"USER_SERVICE_BASE_URL").returns(successful(Seq(user1, user2)))
      val eventualResponse = userService.getUsers
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(Seq(user1, user2))
      }
    }
  }
}
