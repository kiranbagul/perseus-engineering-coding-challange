package routes


import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NotFound}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.typesafe.scalalogging.Logger
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import repository.TemplateRepository
import routes.HTTPResponse.{Error, ErrorResponse}

import scala.util.{Failure, Success}

class TemplateRouter(templateRepository: TemplateRepository) extends Router {

  def log = Logger(classOf[TemplateRouter])

  val exceptionHandler = ExceptionHandler {
    case ex: Exception => complete((StatusCodes.BadRequest, ErrorResponse(Error(ex.getMessage))))
  }

  override val route: Route = pathPrefix("v1" / "templates") {
    handleExceptions(exceptionHandler) {
      get {
        getById ~ getByName
      }
    }
  }

  def getById: Route =
    path("id" / Segment) { id =>
      log.debug(s"Request received for retrieving template by id ${id} ")
      onComplete(templateRepository.byId(id.toLong)) {
        case Success(Some(template)) => complete(template)
        case Success(None) => complete(NotFound, toErrorResponse(s"Template not found for ID ${id}"))
        case Failure(exception) => complete(InternalServerError, toErrorResponse(exception.getMessage))
      }
    }

  def getByName: Route =
    path(Segment) { name =>
      log.debug(s"Request received for retrieving template by name ${name} ")
      onComplete(templateRepository.byName(name)) {
        case Success(Some(template)) => complete(template)
        case Success(None) => complete(NotFound, toErrorResponse(s"Template not found for name ${name}"))
        case Failure(exception) => complete(InternalServerError, toErrorResponse(exception.getMessage))
      }
    }

}
