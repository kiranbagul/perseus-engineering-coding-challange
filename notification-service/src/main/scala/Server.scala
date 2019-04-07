import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.Logger
import routes.Router

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}

class Server(routers: Seq[Router], host: String, port: Int)(
  implicit
  system: ActorSystem,
  ex: ExecutionContext,
  mat: ActorMaterializer
) extends RouteConcatenation {

  def log = Logger(classOf[Server])

  val route = concat(routers.map(_.route): _*)

  def bind = {
    val binding = Http().bindAndHandle(route, host, port)
    binding onComplete {
      case Success(_) => log.info(s"Server online at http://${host}:${port}/")
      case Failure(exception) => log.error(s"Server Failed to start $exception")
    }
    Await.result(binding, 3.second)
  }
}
