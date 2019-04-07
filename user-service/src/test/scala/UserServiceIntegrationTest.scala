import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import domain.User
import io.circe.generic.auto._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import repository.persistance.database.MigrationConfig
import routes.HTTPResponse.ErrorResponse
import routes.toErrorResponse

class UserServiceIntegrationTest extends WordSpec with Matchers with ScalatestRouteTest with Application {

  s"user service" should {

    "successfully get user with ID present in DB" in {
      val user = User(45, "Turner", "Maria", "female", "maTur@somewhere.com", true)
      Get(s"/v1/users/45") ~> server.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[User] shouldBe user
      }
    }

    "404 on get for user not present in DB" in {
      Get(s"/v1/users/100") ~> server.route ~> check {
        status shouldBe StatusCodes.NotFound
        responseAs[ErrorResponse] shouldBe toErrorResponse("User not found for ID 100")
      }
    }

    "successfully get all users present in DB" in {
      Get(s"/v1/users") ~> server.route ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[Seq[User]]
        response.size shouldBe 23
      }
    }

  }
}