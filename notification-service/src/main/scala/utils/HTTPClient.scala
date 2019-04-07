package utils

import akka.actor.ActorSystem
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import domain.{Template, User}
import io.circe.generic.auto._
import net.softler.client.ClientRequest

import scala.concurrent.{ExecutionContext, Future}

//TODO to refactor to type based generic client for user/template service

trait TemplateServiceHTTPClient {
  def getTemplate(url: String): Future[Template]
}


trait UserServiceHTTPClient {
  def getUser(url: String): Future[User]

  def getUsers(url: String): Future[Seq[User]]
}

class UserServiceHTTPClientImpl(implicit system: ActorSystem,
                                materializer: Materializer,
                                executionContext: ExecutionContext) extends UserServiceHTTPClient with FailFastCirceSupport {

  def getUser(url: String): Future[User] = ClientRequest(url).get[User]

  def getUsers(url: String): Future[Seq[User]] = ClientRequest(url).get[Seq[User]]

}

class TemplateServiceHTTPClientImpl(implicit system: ActorSystem,
                                    materializer: Materializer,
                                    executionContext: ExecutionContext) extends TemplateServiceHTTPClient with FailFastCirceSupport {
  def getTemplate(url: String): Future[Template] = ClientRequest(url).get[Template]

}

