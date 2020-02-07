import java.util.Calendar

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}


object FutureFlatMap extends App {

  def getFuture(sleep: Long, text: String): Future[String] = {
    Future {
      Thread.sleep(sleep);
      text
    }
  }


  // Example 1 Both futures are executed sequentially

  println("Before flatmap" + Calendar.getInstance().getTime)
  val res: Future[String] = getFuture(2000, "first message").flatMap(m =>
    getFuture(2000, "second message").map(m2 =>
      s"Result of ${m} and ${m2}"
    )
  )

  res.foreach { case msg =>
    println("After flatmap" + Calendar.getInstance().getTime)
    println(msg) }

  Thread.sleep(6000)

  // Example 2. It's the same to example 1. Both futures are executed sequentially

  println()
  println("Before for" + Calendar.getInstance().getTime)
  val res2 = for {
    m <- getFuture(2000, "first message")
    m2 <- getFuture(2000, "first 2")

  } yield {
    s"Result of ${m} and ${m2} (The same to example1)"
  }

  res2.foreach { case msg =>
    println("After for" + Calendar.getInstance().getTime)
    println(msg) }


  Thread.sleep(6000)


  // Example 3 . Both Futures are executed parallel

  val ff1 = getFuture(2000, "first message")
  val ff2 = getFuture(2000, "first 2")

  println()
  println("Before for" + Calendar.getInstance().getTime)
  val res3 = for {
    m <- ff1
    m2 <- ff2

  } yield {
    s"Result of ${m} and ${m2} (The same to example1)"
  }

  res3.foreach { case msg =>
    println("After for" + Calendar.getInstance().getTime)
    println(msg) }


  Thread.sleep(6000)

}
