package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import domain.User
import io.circe.generic.auto._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}
import repository.UserRepository
import routes.HTTPResponse.ErrorResponse

import scala.concurrent.Future

class UserRouterSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory {

  val userRepository = mock[UserRepository]
  val router = new UserRouter(userRepository)

  s"get User by ID route" should {

    "successfully get user with ID present in DB" in {
      val user = User(1, "", "", "", "", true)
      (userRepository.getUser _).expects(1).returns(Future.successful(Some((user))))
      Get(s"/v1/users/1") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[User] shouldBe user
      }
    }

    "404 on get for user not present in DB" in {
      (userRepository.getUser _).expects(2).returns(Future.successful(None))
      Get(s"/v1/users/2") ~> router.route ~> check {
        status shouldBe StatusCodes.NotFound
        responseAs[ErrorResponse] shouldBe toErrorResponse("User not found for ID 2")
      }
    }

    "500 on get user for error in retrieving user from DB" in {
      (userRepository.getUser _).expects(2).returns(Future.failed(new RuntimeException("Some error ")))
      Get(s"/v1/users/2") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
        responseAs[ErrorResponse] shouldBe toErrorResponse("Some error ")
      }
    }

    "500 on get user for invalid user id" in {
      Get(s"/v1/users/2s") ~> router.route ~> check {
        status shouldBe StatusCodes.BadRequest
        responseAs[ErrorResponse] shouldBe toErrorResponse("For input string: \"2s\"")
      }
    }

  }

  s"get all Users" should {

    "successfully get user with ID present in DB" in {
      val user = User(1, "", "", "", "", true)
      (userRepository.all _).expects().returns(Future.successful(Seq(user)))
      Get(s"/v1/users") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[Seq[User]]
        response shouldBe Seq(user)
        response.size shouldBe 1
      }
    }

    "500 on get user for error in retrieving user from DB" in {
      (userRepository.all _).expects().returns(Future.failed(new RuntimeException("Some error")))
      Get(s"/v1/users") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
        responseAs[ErrorResponse] shouldBe toErrorResponse("Some error")
      }
    }

  }

}