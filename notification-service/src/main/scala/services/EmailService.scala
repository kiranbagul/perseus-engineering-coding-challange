package services

import com.typesafe.scalalogging.Logger
import domain.Email
import utils.EmailServer

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

trait EmailService {

  def send(email: Email): Future[Boolean]

  def batch(emails: Seq[Email]): Unit

}

class EmailServiceImpl(server: EmailServer) extends EmailService{

  def log = Logger(classOf[EmailService])

  private var emailQueue = new ArrayBuffer[Email]()

  def send(email: Email): Future[Boolean] = server.send(email)

  def batch(emails: Seq[Email]): Unit = {
    this.synchronized {
      emailQueue = emailQueue ++ emails
      val sent = emailQueue.grouped(10)
        .filter(_.size == 10)
        .map(group => {
          sendBatch(group)
          group
        }).toList
      sent.foreach(group => emailQueue --= group)
    }
  }

  private def sendBatch(emails: ArrayBuffer[Email]) = server.batch(emails)

}
