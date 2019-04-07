package utils

import com.typesafe.scalalogging.Logger
import domain.Email

import scala.concurrent.Future

class EmailServer {

  def log = Logger(classOf[EmailServer])

  def send(email: Email): Future[Boolean] = {
    sendEmail(email)
    Future.successful(true)
  }

  def batch(emails: Seq[Email]): Future[Boolean] = {
    emails.foreach(email => sendEmail(email))
    Future.successful(true)
  }

  private def sendEmail(email: Email) = {
    log.info(s"\n\n\nSending email to ${email.emailId} with body as ${email.body}\n\n\n")
  }
}
