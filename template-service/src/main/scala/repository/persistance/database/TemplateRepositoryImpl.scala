package repository.persistance.database

import domain.Template
import repository.TemplateRepository
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._

class TemplateRepositoryImpl(collection: Seq[Template] = Seq.empty) extends BaseDao with TemplateRepository {

  val templatesTable = TableQuery[TemplatesTable]

  Await.result(templatesTable ++= collection, 10.seconds)

  override def byId(id: Long): Future[Option[Template]] = templatesTable.filter(_.id === id).result.headOption

  override def byName(name: String): Future[Option[Template]] = templatesTable.filter(_.name === name).result.headOption
}