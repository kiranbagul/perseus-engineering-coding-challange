package services

import domain.Template
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatest.{Matchers, WordSpec}
import utils.TemplateServiceHTTPClient

import scala.concurrent.Await
import scala.concurrent.Future.successful
import scala.concurrent.duration.Duration

class TemplateServiceTest extends WordSpec with Matchers with MockFactory {

  "TemplateService" should {

    "retrieve welcome template for new user created trigger" in {
      val client = mock[TemplateServiceHTTPClient]
      val welcomeTemplate = new Template(1, "welcome", "body")
      (client.getTemplate _).expects(s"TEMPLATE_SERVICE_BASE_URL/welcome").returns(successful(welcomeTemplate))
      val eventualResponse = new TemplateServiceImpl(client).getWelcomeTemplate
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(welcomeTemplate)
      }
    }

    "retrieve newsletter template for sending newsletters" in {
      val client = mock[TemplateServiceHTTPClient]
      val newsletterTemplate = new Template(1, "newsletter", "body")
      (client.getTemplate _).expects(s"TEMPLATE_SERVICE_BASE_URL/newsletter").returns(successful(newsletterTemplate))
      val eventualResponse = new TemplateServiceImpl(client).getNewsletterTemplate
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(newsletterTemplate)
      }
    }
  }

}
