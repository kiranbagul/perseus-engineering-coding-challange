package routes

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives.{get, path}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete

class HealthRouter extends Router {

  override lazy val route: Route =
    get {
      path("health") {
        complete(OK)
      }
    }
}
