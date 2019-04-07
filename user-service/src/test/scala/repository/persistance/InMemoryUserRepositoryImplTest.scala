package repository.persistance

import domain.User
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class InMemoryUserRepositoryImplTest extends WordSpec with Matchers {

  private val users = Seq(User(1, "a", "b", "c", "d", true), User(2, "a", "b", "c", "d", true))
  private val repository = new InMemoryUserRepositoryImpl(users)

  s"Get user" should {

    "retrieve successfully user by user id" in {
      val eventualResponse = repository.getUser(1)
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(Some(User(1, "a", "b", "c", "d", true)))
      }
    }

    "return empty get user called by non existing user id" in {
      val eventualResponse = repository.getUser(5)
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(None)
      }
    }
  }

  s"Get all users" should {

    "retrieve all users successfully" in {
      val eventualResponse = repository.all
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(users)
      }
    }

  }

}
