import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import domain.Template
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import routes.HTTPResponse.ErrorResponse
import routes.toErrorResponse
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import repository.persistance.database.MigrationConfig

class TemplateServiceIntegrationSpec extends WordSpec with Matchers with ScalatestRouteTest with Application {

  s"Template service" should {

    "successfully get template with valid template ID " in {
      Get(s"/v1/templates/id/1") ~> server.route ~> check {
        status shouldBe StatusCodes.OK
        val template = responseAs[Template]
        template.id shouldBe 1
        template.name shouldBe "newsletter"
      }
    }

    "404 on get for invalid template id" in {
      Get(s"/v1/templates/id/21") ~> server.route ~> check {
        status shouldBe StatusCodes.NotFound
        responseAs[ErrorResponse] shouldBe toErrorResponse("Template not found for ID 21")
      }
    }

    "successfully get welcome template" in {
      Get(s"/v1/templates/welcome") ~> server.route ~> check {
        status shouldBe StatusCodes.OK
        val template = responseAs[Template]
        template.id shouldBe 2
        template.name shouldBe "welcome"
      }
    }

    "successfully get newsletter template" in {
      val template = Template(1, "id 1", "Body 1")
      Get(s"/v1/templates/newsletter") ~> server.route ~> check {
        status shouldBe StatusCodes.OK
        val template = responseAs[Template]
        template.id shouldBe 1
        template.name shouldBe "newsletter"
      }
    }


    "404 on get for template name not present in DB" in {
      Get(s"/v1/templates/welcome1") ~> server.route ~> check {
        status shouldBe StatusCodes.NotFound
        responseAs[ErrorResponse] shouldBe toErrorResponse("Template not found for name welcome1")
      }
    }

  }
}
