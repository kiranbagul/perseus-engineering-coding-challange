package routes


import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NotFound}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.typesafe.scalalogging.Logger
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import repository.UserRepository
import routes.HTTPResponse.{Error, ErrorResponse}

import scala.util.{Failure, Success}

class UserRouter(userRepository: UserRepository) extends Router {

  def log = Logger(classOf[UserRouter])

  val exceptionHandler = ExceptionHandler {
    case ex: Exception => complete((StatusCodes.BadRequest, ErrorResponse(Error(ex.getMessage))))
  }

  override val route: Route = pathPrefix("v1") {
    handleExceptions(exceptionHandler) {
      get {
        getUserById ~ getAll
      }
    }
  }

  def getAll: Route =
    path("users") {
      log.debug("Request received for retrieving all users")
      onComplete(userRepository.all) {
        case Success(users) => complete(users)
        case Failure(ex) => {
          log.error("Error retrieving users", ex)
          complete(InternalServerError, toErrorResponse(ex.getMessage))
        }
      }
    }

  def getUserById: Route =
    path("users" / Segment) { userId =>
      log.debug(s"Request received for retrieving user ${userId} ")
      onComplete(userRepository.getUser(userId.toLong)) {
        case Success(Some(user)) => complete(user)
        case Success(None) => complete(NotFound, toErrorResponse(s"User not found for ID ${userId}"))
        case Failure(ex) => {
          log.error(s"Error retrieving user ${userId}", ex)
          complete(InternalServerError, toErrorResponse(ex.getMessage))
        }
      }
    }

}
