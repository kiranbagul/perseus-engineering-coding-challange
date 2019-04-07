package repository.persistance

import domain.Template
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class InMemoryTemplateRepositoryImplTest extends WordSpec with Matchers {

  private val templates = Seq(Template(1, "name 1", "body 1"), Template(2, "name 2", "body 2"))
  private val repository = InMemoryTemplateRepositoryImpl(templates)

  s"Template repository" should {

    "retrieve successfully template by id" in {
      val eventualResponse = repository.byId(1)
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(Some(Template(1, "name 1", "body 1")))
      }
    }

    "retrieve successfully template by name" in {
      val eventualResponse = repository.byName("name 2")
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(Some(Template(2, "name 2", "body 2")))
      }
    }

    "return empty on no template by name" in {
      val eventualResponse = repository.byName("name 3")
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(None)
      }
    }

    "return empty on no template by id" in {
      val eventualResponse = repository.byId(3)
      Await.result(eventualResponse, Duration.Inf)
      whenReady(eventualResponse) { response =>
        response should be(None)
      }
    }
  }

}
