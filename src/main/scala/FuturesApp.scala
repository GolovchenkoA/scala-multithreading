import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object FuturesApp extends App {

  println("hi")

  // Methods

  def getFuture(sleep: Long, text: String): Future[String] = {
    Future {
      Thread.sleep(sleep); text
    }
  }

  def getFutureWithException(sleep: Long, message: String) = {
    Future {
      Thread.sleep(sleep); throw new RuntimeException(message)
    }
  }

  def getFutureOrFutureException(sleep: Long, message: String, thowException: Boolean): Future[Try[String]] = {

    if (thowException) {
      Future {
        Thread.sleep(sleep); throw new RuntimeException(message)
      }
    } else {
      Future {
        Thread.sleep(sleep); Try(message)
      }
    }
  }



  // Checks

  // CHECK WITH for-yield
  val rez = for {
    c1 <- getFuture(1000, "1")
    c2 <- getFuture(1000, "2")
    //c3 <- getFutureWithException(1000, "my exception") // will be Exception
  } yield (c1, c2)


  rez.onComplete {
    case Success(value) => println(value._1 + ", " + value._2)
    case Failure(exception) => println("Exception")
  }

  // second call
  rez.foreach {
    case r => println(r._1)
  }

  // Wait for futures
  Thread.sleep(4000)


  // Check with Future.sequence


  val rez2 = Future.sequence(Seq(
    getFuture(500, "1"),
    getFuture(500, "2"),
    getFutureWithException(100, "my Exception 2")
  ))


  rez2.failed foreach {
    case t => println(s"rez2 failed: ${t.getMessage}" )
  }


  // rez2.foreach or onSuccess will be executed only if there is not any failures by onComplete has case Failure
  rez2.onComplete {
    case Success(value) => println(value.mkString(", "))
    case Failure(exception) => println("Exception 2")
    case NonFatal(e) => println("non fatal")
  }


  Thread.sleep(1000)

  // Future with Try


  getFutureOrFutureException(500, "no exception", false).foreach {
    case result => println(s"Future with try: ${result.get}")
  }

  getFutureOrFutureException(500, "with  exception", true).failed foreach {
    case result => println(s"Future with try: ${result.getMessage}")
  }

  Thread.sleep(1500)


  // Try -> Success or Failure


  def handleMessage(t: Try[String]) = t match {
    case Success(value) => println(value)
    case Failure(exception) => println(s"Exception  ${exception.getMessage}")

  }

  val msg: Try[String] = for {
    m <- Try("try message")
    threadName <- Try(Thread.currentThread().getName)
    // It returns exception thus msg will be Failure (i.e Try[Throwable])
    //ex <- Try(throw new RuntimeException("Exception in for-yield with Try"))
  } yield s"Message ${m} was created from thread ${threadName}"

  handleMessage(msg)

  // handle exception
  handleMessage(Try(throw new RuntimeException("Big bada boom")))

/*

  // filter failed futures

  val multiFutures: Seq[Try[Future[String]]] = Seq(
    Try(getFuture(500, "1")),
    Try(getFuture(500, "2")),
    // will be filtered
    Try(getFutureWithException(100, "my Exception 2"))
  )

  val validFutures: Seq[Future[String]] = for {
    f <- multiFutures
    //f.isCompleted &&
    if f.isSuccess

  } yield f.get


  println( s"Valid result 0: ${validFutures(0).value.get.get}")
  println( s"Valid result 1: ${validFutures(1).value.get.get}")
*/

    Thread.sleep(2000)


}
