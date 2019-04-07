package repository.persistance

import domain.Template
import repository.TemplateRepository

import scala.concurrent.Future

class InMemoryTemplateRepositoryImpl(collection: Seq[Template] = Seq.empty) extends TemplateRepository {

  override def byId(id: Long): Future[Option[Template]] = Future.successful(collection.find(_.id.equals(id)))

  override def byName(name: String): Future[Option[Template]] = Future.successful(collection.find(_.name.equals(name)))
}

object InMemoryTemplateRepositoryImpl {
  def apply(collection: Seq[Template] = Seq.empty): InMemoryTemplateRepositoryImpl = {
    new InMemoryTemplateRepositoryImpl(collection)
  }
}
