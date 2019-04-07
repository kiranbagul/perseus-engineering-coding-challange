package utils

import domain.{Template, User}

class EmailBuilder(tmpl : Template) {

  def of(user: User): String = {
    tmpl.body.replaceAll("user.salutation", if (user.gender.equals("Male")) "Mr" else "Miss")
      .replaceAll("user.name", user.firstName)
      .replaceAll("user.identifier", user.id.toString)
      .replaceAll("[{}]", "")
  }

}
