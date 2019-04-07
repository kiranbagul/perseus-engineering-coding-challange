package services

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import domain.Template
import utils.TemplateServiceHTTPClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait TemplateService {

  def getWelcomeTemplate: Future[Template]

  def getNewsletterTemplate: Future[Template]
}

class TemplateServiceImpl(httpClient: TemplateServiceHTTPClient) extends TemplateService {

  def log = Logger(classOf[TemplateService])

  private lazy val templateServiceUrl = {
    val config = ConfigFactory.load()
    config.getString("templateServiceUrl")
  }

  override def getWelcomeTemplate: Future[Template] = getTemplate("welcome")

  override def getNewsletterTemplate: Future[Template] = getTemplate("newsletter")

  private def getTemplate(name: String): Future[Template] = {
    val futureTemplate = httpClient.getTemplate(templateServiceUrl + "/" + name)
    futureTemplate recoverWith {
      case ex =>
        log.error("Error in retrieving template : ", ex)
        Future.failed(throw new RuntimeException(ex))
    }
  }

}
