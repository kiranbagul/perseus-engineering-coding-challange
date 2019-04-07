package repository.persistance.database

import domain.User
import slick.jdbc.MySQLProfile.api._

class UsersTable(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Long]("ID")
  def surName = column[String]("SURNAME")
  def firstName = column[String]("FIRSTNAME")
  def gender = column[String]("GENDER")
  def email = column[String]("EMAIL")
  def subscribedNewsletter = column[Boolean]("SUBSCRIBED_NEWS_LETTER")
  def * = (id, surName, firstName, gender, email, subscribedNewsletter) <> ((User.apply _).tupled, User.unapply)
}