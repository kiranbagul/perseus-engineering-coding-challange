import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import routes.{HealthRouter, NotificationRouter}
import services._
import utils.{EmailServer, TemplateServiceHTTPClientImpl, UserServiceHTTPClientImpl}

trait Application {

  def log = Logger(classOf[Application])

  implicit val system = ActorSystem("notification-service")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  private lazy val config = ConfigFactory.load()
  private lazy val ip = config.getString("http.ip")
  private lazy val port = config.getInt("http.port")

  private val templateService: TemplateService = new TemplateServiceImpl(new TemplateServiceHTTPClientImpl)
  private val userService: UserService = new UserServiceImpl(new UserServiceHTTPClientImpl)
  private val emailService: EmailService = new EmailServiceImpl(new EmailServer)
  private lazy val notificationService = new NotificationService(templateService, userService, emailService)
  private lazy val server = new Server(Seq(new NotificationRouter(notificationService), new HealthRouter()), ip, port)

  def start = server.bind

}

object Main extends Application with App {
  start
}


