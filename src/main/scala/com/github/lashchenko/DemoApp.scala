package com.github.lashchenko

import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import net.codingwell.scalaguice.ScalaModule

////////////////////////////////////////////////////////////////////////////////
// DEMO APP
////////////////////////////////////////////////////////////////////////////////

object DemoApp extends App {

  val injector: ScalaInjector = {
    Guice.createInjector(new ScalaModule {
      override def configure() = {
        bind[DemoDbComponent].to[SQLiteDbComponent].asEagerSingleton()
        bind[DemoDataDao].asEagerSingleton()
        bind[DemoDataDbService].asEagerSingleton()
      }
    })
  }

  import scala.concurrent.ExecutionContext.Implicits.global

  val demoService: DemoDataDbService = injector.instance[DemoDataDbService]

  println("Press enter to create demo table")
  System.in.read()
  demoService.create().onComplete { case x => println(x); println("\nPress enter to add test data to demo table") }

  System.in.read()
  demoService.addTestData().onComplete { case x => println(x); println("\nPress enter to findAll() test data") }

  System.in.read()
  demoService.findAll().onComplete { case x => println(x); println("\nPress enter to findAllEnabled() test data") }

  System.in.read()
  demoService.findAllEnabled().onComplete { case x => println(x); println("\nPress enter to findByNameLength(2) test data") }

  System.in.read()
  demoService.findByNameLength(2).onComplete { case x => println(x); println("\nPress enter to drop demo table") }

  System.in.read()
  demoService.drop().onComplete { case x => println(x); println("\nPress enter to exit") }

  System.in.read()

}
