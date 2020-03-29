package com.github.lashchenko

import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import net.codingwell.scalaguice.ScalaModule
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatest.matchers.should.Matchers._

class DemoDataDbServiceSpec extends AsyncWordSpec {

  val injector: ScalaInjector = Guice.createInjector(new ScalaModule {
    override def configure() = {
      bind[DemoDbComponent].to[H2DbComponent].asEagerSingleton()
      bind[DemoDataDao].asEagerSingleton()
      bind[DemoDataDbService].asEagerSingleton()
    }
  })

  val demoDb: DemoDataDbService = injector.instance[DemoDataDbService]

  "DemoDataDbService" should {

    "return empty seq from new table for findAll() query" in {
      demoDb.create()
        .flatMap { _ => demoDb.findAll() }
        .andThen { case _ => demoDb.drop() }
        .map(result => result shouldBe empty)
    }

    "return all records from test non empty table for findAll query" in {
      demoDb.create()
        .flatMap { _ => demoDb.addTestData() }
        .flatMap { _ => demoDb.findAll() }
        .andThen { case _ => demoDb.drop() }
        .map { result =>
          result should contain only (
            DemoData(DemoId(1, "A"), Some(1024), Some("A"), enabled = true),
            DemoData(DemoId(2, "B"), Some(2048), Some("Bb"), enabled = false),
            DemoData(DemoId(3, "C"), Some(4096), Some("Ccc"), enabled = true),
            DemoData(DemoId(4, "D"), None, None, enabled = false)
          )
        }
    }

    "return 1A and 3C records from test non empty table for findAllEnabled query" in {
      demoDb.create()
        .flatMap { _ => demoDb.addTestData() }
        .flatMap { _ => demoDb.findAllEnabled() }
        .andThen { case _ => demoDb.drop() }
        .map { result =>
          result should contain only (
            DemoData(DemoId(1, "A"), Some(1024), Some("A"), enabled = true),
            DemoData(DemoId(3, "C"), Some(4096), Some("Ccc"), enabled = true)
          )
        }
    }

    "return 2B record from test non empty table for findByNameLength(2) query" in {
      demoDb.create()
        .flatMap { _ => demoDb.addTestData() }
        .flatMap { _ => demoDb.findByNameLength(2) }
        .andThen { case _ => demoDb.drop() }
        .map { result => result should contain only DemoData(DemoId(2, "B"), Some(2048), Some("Bb"), enabled = false) }
    }

    "return 1 for plainSqlCountByName(A) query" in {
      demoDb.create()
        .flatMap { _ => demoDb.addTestData() }
        .flatMap { _ => demoDb.plainSqlCountByName("A") }
        .andThen { case _ => demoDb.drop() }
        .map { result => result shouldBe Some(1) }
    }

    "return custom fields for plainSqlCustomResult() query" in {
      demoDb.create()
        .flatMap { _ => demoDb.addTestData() }
        .flatMap { _ => demoDb.plainSqlCustomResult() }
        .andThen { case _ => demoDb.drop() }
        .map { result =>
          result should contain only (
            (Some(1024), Some("A")),
            (Some(2048), Some("Bb")),
            (Some(4096), Some("Ccc")),
            (None, None)
          )
        }
    }

  }

}
