package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import domain.Template
import io.circe.generic.auto._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}
import repository.TemplateRepository
import routes.HTTPResponse.ErrorResponse

import scala.concurrent.Future

class TemplateRouterSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory {

  val templateRepository = mock[TemplateRepository]
  val router = new TemplateRouter(templateRepository)

  s"get Template by ID route" should {

    "successfully get template with ID present in DB" in {
      val template = Template(1, "id 1", "Body 1")
      (templateRepository.byId _).expects(1).returns(Future.successful(Some(template)))
      Get(s"/v1/templates/id/1") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[Template] shouldBe template
      }
    }

    "404 on get for template not present in DB" in {
      (templateRepository.byId _).expects(2).returns(Future.successful(None))
      Get(s"/v1/templates/id/2") ~> router.route ~> check {
        status shouldBe StatusCodes.NotFound
        responseAs[ErrorResponse] shouldBe toErrorResponse("Template not found for ID 2")
      }
    }

    "500 on get template on error in retrieving template from DB" in {
      (templateRepository.byId _).expects(2).returns(Future.failed(new RuntimeException("Some error ")))
      Get(s"/v1/templates/id/2") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
        responseAs[ErrorResponse] shouldBe toErrorResponse("Some error ")
      }
    }

    "500 on get for invalid template id" in {
      Get(s"/v1/templates/id/2s") ~> router.route ~> check {
        status shouldBe StatusCodes.BadRequest
        responseAs[ErrorResponse] shouldBe toErrorResponse("For input string: \"2s\"")
      }
    }

  }

  s"get Template by name" should {

    "successfully get template with name present in DB" in {
      val template = Template(1, "name1", "Body 1")
      (templateRepository.byName _).expects("name1").returns(Future.successful(Some(template)))
      Get(s"/v1/templates/name1") ~> router.route ~> check {
        status shouldBe StatusCodes.OK
        responseAs[Template] shouldBe template
      }
    }
  }

}