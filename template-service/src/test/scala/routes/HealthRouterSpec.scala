package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class HealthRouterSpec extends WordSpec with Matchers with ScalatestRouteTest {

  val router = new HealthRouter()

  s"health route" should {

    "successful get" in {
      Get(s"/health") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe "OK"
      }
    }
  }

}