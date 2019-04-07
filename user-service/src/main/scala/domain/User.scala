package domain

case class User(id: Long,
                surName: String,
                firstName: String,
                gender: String,
                email: String,
                subscribedNewsletter: Boolean)
