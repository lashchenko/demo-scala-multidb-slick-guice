package com.github.lashchenko

import com.google.inject.Inject
import slick.jdbc.GetResult
import slick.sql.SqlProfile.ColumnOption.Nullable

import scala.concurrent.{ExecutionContext, Future}

////////////////////////////////////////////////////////////////////////////////
// TABLES AND DAOs
////////////////////////////////////////////////////////////////////////////////

class DemoDataDao @Inject()(val dbComponent: DemoDbComponent) {

  import dbComponent._
  import profile.api._

  class DemoDataTable(tag: Tag) extends Table[DemoData](tag, "demo") {

    def iId = column[Int]("iId")
    def sId = column[String]("sId", O.Length(10))

    def count = column[Int]("count", Nullable)
    def name = column[String]("name", Nullable)
    def enabled = column[Boolean]("enabled")

    def pk = primaryKey("pk_id", (iId, sId))
    def idx = index("idx_a", (iId, sId), unique = true)

    def demoId = (iId, sId) <> (DemoId.tupled, DemoId.unapply)
    def * = (demoId, count.?, name.?, enabled) <> (DemoData.tupled, DemoData.unapply)
  }

  val demoDataTable = TableQuery[DemoDataTable]
}

class DemoDataDbService @Inject()(val dao: DemoDataDao) {
  import dao._
  import dbComponent._
  import profile.api._

  def create()(implicit ec: ExecutionContext) = {
    val q = demoDataTable.schema.create
    db.run(q)
  }

  def addTestData()(implicit ec: ExecutionContext) = {
    val q = DBIO.seq(
      demoDataTable += DemoData(DemoId(1, "A"), Some(1024), Some("A"), enabled = true),
      demoDataTable += DemoData(DemoId(2, "B"), Some(2048), Some("Bb"), enabled = false),
      demoDataTable += DemoData(DemoId(3, "C"), Some(4096), Some("Ccc"), enabled = true),
      demoDataTable += DemoData(DemoId(4, "D"), None, None, enabled = false)
    )
    db.run(q)
  }

  def drop()(implicit ec: ExecutionContext) = {
    val q = demoDataTable.schema.drop
    db.run(q)
  }

  def findAll()(implicit ec: ExecutionContext): Future[Seq[DemoData]] = {
    db.run(demoDataTable.result)
  }

  def findAllEnabled()(implicit ec: ExecutionContext): Future[Seq[DemoData]] = {
    db.run(demoDataTable.filter(_.enabled).result)
  }

  def findByNameLength(len: Int)(implicit ec: ExecutionContext): Future[Seq[DemoData]] = {
    db.run(demoDataTable.filter(_.name.length === len).result)
  }

  def plainSqlCountByName(name: String)(implicit ec: ExecutionContext) = {
    val q = sql"SELECT count(*) FROM demo where name = $name".as[Int].headOption
    db.run(q)
  }

  def plainSqlCustomResult()(implicit ec: ExecutionContext) = {
    type CustomType = (Option[Int], Option[String])
    implicit val sqlResToIntStr = GetResult { r => (r.<<[Option[Int]], r.<<[Option[String]]) }
    val q = sql"""SELECT "count", "name" FROM demo""".as[CustomType]
    db.run(q)
  }

}
