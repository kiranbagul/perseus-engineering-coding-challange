import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import routes.Router

import scala.concurrent.{ExecutionContext, Future}

class Server(routers: Seq[Router], host: String, port: Int)(
    implicit
    system: ActorSystem,
    ex: ExecutionContext,
    mat: ActorMaterializer
) extends RouteConcatenation {

  val route = concat(routers.map(_.route): _*)

  def bind: Future[ServerBinding] = Http().bindAndHandle(route, host, port)

}
