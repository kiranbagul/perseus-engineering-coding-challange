package services

import domain.Email
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import utils.EmailServer

import scala.concurrent.Future

class EmailServiceTest extends WordSpec with Matchers with MockFactory {

  "Email service" should {
    "send a email" in {
      val server = mock[EmailServer]
      val service = new EmailServiceImpl(server)
      val email = new Email("id", "body")
      (server.send _).expects(email).returns(Future.successful(true))
      service.send(email)
    }

    "do not send email if newsletter email count is less than 10" in {
      val server = mock[EmailServer]
      val service = new EmailServiceImpl(server)
      val email1 = new Email("id1", "body")
      val email2 = new Email("id2", "body")
      val emails = Seq(email1, email2)
      service.batch(emails)
    }

    "send newsletter if email count is 10 " in {
      val server = mock[EmailServer]
      val service = new EmailServiceImpl(server)
      val emails = Range(0, 10).map(i => new Email(s"id${i}", "body"))
      (server.batch _).expects(emails).returns(Future.successful(true))
      service.batch(emails)
    }

    "send emails when email count is reached to 10 in susequent calls" in {
      val server = mock[EmailServer]
      val service = new EmailServiceImpl(server)
      val firstBatchEmails = Range(0, 7).map(i => new Email(s"id${i}", "body"))
      val secondBatchEmails = Range(7, 13).map(i => new Email(s"id${i}", "body"))
      (server.batch _).expects(Range(0, 10).map(i => new Email(s"id${i}", "body"))).returns(Future.successful(true))
      service.batch(firstBatchEmails)
      service.batch(secondBatchEmails)
    }

  }
}
