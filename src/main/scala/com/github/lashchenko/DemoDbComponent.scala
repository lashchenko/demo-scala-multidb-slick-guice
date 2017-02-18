package com.github.lashchenko

////////////////////////////////////////////////////////////////////////////////
// DB COMPONENTS
////////////////////////////////////////////////////////////////////////////////

trait DemoDbComponent {
  import slick.driver.JdbcDriver
  val driver: JdbcDriver

  import driver.api._
  val db: Database
}

class MySqlDbComponent extends DemoDbComponent {
  import slick.driver.MySQLDriver.api._

  val driver = slick.driver.MySQLDriver
  val db = Database.forConfig("mysql")
}

class SQLiteDbComponent extends DemoDbComponent {
  import slick.driver.SQLiteDriver.api._

  val driver = slick.driver.SQLiteDriver
  val db = Database.forConfig("sqlite")
}

class H2DbComponent extends DemoDbComponent {
  import slick.driver.H2Driver.api._

  val driver = slick.driver.H2Driver
  val db = Database.forConfig("h2")
}
