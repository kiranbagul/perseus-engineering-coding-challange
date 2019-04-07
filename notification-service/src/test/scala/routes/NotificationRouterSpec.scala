package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import domain.UserCreated
import io.circe.generic.auto._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}
import services._

import scala.concurrent.Future

class NotificationRouterSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory {

  private val notificationService = mock[NotificationService]

  val router = new NotificationRouter(notificationService)

  "Notification route" should {
    "successfully send welcome email to new user created" in {
      val request = UserCreated(46)
      (notificationService.welcome _).expects(46).returns(Future.successful(true))
      Post(s"/v1/notifications", request) ~> router.route ~> check {
        status shouldBe StatusCodes.OK
      }
    }

    "respond with bad request on welcome email trigger to non existent user " in {
      val request = UserCreated(46)
      (notificationService.welcome _).expects(46).returns(Future.successful(false))
      Post(s"/v1/notifications", request) ~> router.route ~> check {
            status shouldBe StatusCodes.BadRequest
      }
    }

    "respond with InternalServerError request on welcome email trigger to non existent user " in {
      val request = UserCreated(46)
      (notificationService.welcome _).expects(46).returns(Future.failed(new RuntimeException()))
      Post(s"/v1/notifications", request) ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }

    "successfully send newsletters to all users" in {
      (notificationService.newsletter _).expects().returns(Future.successful())
      Post(s"/v1/notifications/newsletter") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
      }
    }

    "respond with InternalServerError on newsletter trigger" in {
      (notificationService.newsletter _).expects().returns(Future.failed(new RuntimeException()))
      Post(s"/v1/notifications/newsletter") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }

  }

}