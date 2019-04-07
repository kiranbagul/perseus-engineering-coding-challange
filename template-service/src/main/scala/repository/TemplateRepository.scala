package repository

import domain.Template

import scala.concurrent.Future

trait TemplateRepository {

  def byId(id: Long): Future[Option[Template]]

  def byName(name : String): Future[Option[Template]]

}
