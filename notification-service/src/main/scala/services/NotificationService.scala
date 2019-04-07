package services

import com.typesafe.scalalogging.Logger
import domain.Email
import utils.EmailBuilder

import scala.concurrent.ExecutionContext.Implicits.global

class NotificationService(templateService: TemplateService, userService: UserService, emailService: EmailService) {
  def log = Logger(classOf[NotificationService])

  def welcome(id: Long) = {
    templateService.getWelcomeTemplate flatMap { template =>
      userService.getUser(id) flatMap { user =>
        emailService send Email(user.email, new EmailBuilder(template).of(user))
      }
    }
  }

  def newsletter() = {
    templateService.getNewsletterTemplate flatMap { template =>
      userService.getUsers map { users =>
        emailService batch users.filter(_.subscribedNewsletter)
          .map(user => Email(user.email, new EmailBuilder(template).of(user)))
      }
    }
  }
}
