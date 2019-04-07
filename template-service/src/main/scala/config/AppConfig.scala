package config

import com.typesafe.config.ConfigFactory

trait AppConfig {
  private val config = ConfigFactory.load()
  val hostIp = config.getString("http.ip")
  val hostPort = config.getInt("http.port")
  val mode = config.getBoolean("db.in-memory")
  val url = config.getString("db.sql.url")
  val user = config.getString("db.sql.user")
  val password = config.getString("db.sql.password")
}
