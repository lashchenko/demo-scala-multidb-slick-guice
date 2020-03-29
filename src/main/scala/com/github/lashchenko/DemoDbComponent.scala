package com.github.lashchenko

////////////////////////////////////////////////////////////////////////////////
// DB COMPONENTS
////////////////////////////////////////////////////////////////////////////////

trait DemoDbComponent {
  import slick.jdbc.JdbcProfile
  val profile: JdbcProfile

  import profile.api._
  val db: Database
}

class MySqlDbComponent extends DemoDbComponent {
  import slick.jdbc.MySQLProfile.api._

  val profile = slick.jdbc.MySQLProfile
  val db = Database.forConfig("mysql")
}

class SQLiteDbComponent extends DemoDbComponent {
  import slick.jdbc.SQLiteProfile.api._

  val profile = slick.jdbc.SQLiteProfile
  val db = Database.forConfig("sqlite")
}

class H2DbComponent extends DemoDbComponent {
  import slick.jdbc.H2Profile.api._

  val profile = slick.jdbc.H2Profile
  val db = Database.forConfig("h2")
}
