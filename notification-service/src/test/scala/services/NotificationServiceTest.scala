package services

import domain.{Email, Template, User}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future.successful
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class NotificationServiceTest extends WordSpec with Matchers with MockFactory {

  private val userService = mock[UserService]
  private val templateService = mock[TemplateService]
  private val emailService = mock[EmailService]
  private val notificationService = new NotificationService(templateService, userService, emailService)

  "notification service" should {
    "retrieve welcome email template, user information and trigger email when user is created" in {
      val welcomeTemplate = new Template(1, "welcome", "{{}}")
      val user = new User(1, "s", "f", "M", "e", true)
      (templateService.getWelcomeTemplate _).expects().returns(successful(welcomeTemplate))
      (userService.getUser _).expects(1).returns(successful(user))
      (emailService.send _).expects(where { email: Email => true }).returns(successful(true))

      val eventualResponse: Future[Boolean] = notificationService.welcome(1)
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(true)
      }
    }

    "retrieve newsletter template, users and trigger email only when user is subscribed to newsletter" in {
      val newsletterTemplate = new Template(1, "newsletter", "")
      val user1 = new User(1, "s", "f", "M", "e1", false)
      val user2 = new User(2, "s", "f", "M", "e2", true)
      val user3 = new User(3, "s", "f", "M", "e3", true)
      val user4 = new User(4, "s", "f", "M", "e4", false)
      (templateService.getNewsletterTemplate _).expects().returns(successful(newsletterTemplate))
      (userService.getUsers _).expects().returns(successful(Seq(user1, user2, user3, user4)))
      (emailService.batch _).expects(where { emails: Seq[Email] =>
        emails.size == 2 && emails.contains(Email("e2", "")) && emails.contains(Email("e3", ""))
      }).returns(successful(true))

      val eventualResponse = notificationService.newsletter()
      Await.result(eventualResponse, Duration.Inf)
    }
  }


}
