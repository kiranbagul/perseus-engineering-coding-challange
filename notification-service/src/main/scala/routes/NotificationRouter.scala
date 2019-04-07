package routes


import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.typesafe.scalalogging.Logger
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import domain.UserCreated
import io.circe.generic.auto._
import domain.HTTPResponse.{Error, ErrorResponse}
import services.NotificationService

import scala.util.{Failure, Success}

class NotificationRouter(notificationService: NotificationService) extends Router {

  def log = Logger(classOf[NotificationRouter])

  val exceptionHandler = ExceptionHandler {
    case ex: Exception => complete((StatusCodes.BadRequest, ErrorResponse(Error(ex.getMessage))))
  }

  override val route: Route = pathPrefix("v1") {
    handleExceptions(exceptionHandler) {
      post {
        triggerNotification
      }
    }
  }

  def triggerNotification: Route =
    path("notifications" / "newsletter") {
      onComplete(notificationService.newsletter()) {
        case Success(value) => complete(OK)
        case Failure(exception) => complete(InternalServerError, toErrorResponse(exception.getMessage))
      }
    } ~
      path("notifications") {
        entity(as[UserCreated]) { req =>
          log.debug(s"Request received for triggering welcome email for ${req.ID} ")
          onComplete(notificationService.welcome(req.ID)) {
            case Success(true) => complete(OK)
            case Success(false) => complete(StatusCodes.BadRequest)
            case Failure(exception) => complete(InternalServerError, toErrorResponse(exception.getMessage))
          }
        }
      }

}
