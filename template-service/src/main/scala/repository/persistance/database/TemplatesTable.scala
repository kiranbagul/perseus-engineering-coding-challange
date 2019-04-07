package repository.persistance.database

import domain.Template
import slick.jdbc.MySQLProfile.api._

class TemplatesTable(tag: Tag) extends Table[Template](tag, "TEMPLATES") {
  def id = column[Long]("ID")
  def name = column[String]("NAME")
  def body = column[String]("BODY")
  def * = (id, name, body) <> ((Template.apply _).tupled, Template.unapply)
}