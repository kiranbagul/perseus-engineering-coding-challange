import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.Logger
import config.AppConfig
import repository.PersistenceFactory.getRepository
import routes.{HealthRouter, TemplateRouter}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

trait Application extends AppConfig {

  def log = Logger(classOf[Application])

  private implicit val system = ActorSystem("template-service")
  private implicit val executor = system.dispatcher
  private implicit val materializer = ActorMaterializer()

  lazy val server = new Server(Seq(new TemplateRouter(getRepository()), new HealthRouter()), hostIp, hostPort)

  private lazy val bindServer = {
    val binding = server.bind
    binding.onComplete {
      case Success(_) => log.info(s"Server online at http://${hostIp}:${hostPort}/")
      case Failure(exception) => log.error(s"Server Failed to start $exception")
    }
    Await.result(binding, 3.seconds)
  }

  def start = bindServer

}

object Main extends Application with App {
  start
}


