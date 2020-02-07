import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.control.NonFatal

object FutureRecover extends App {

  def getFutureWithException(sleep: Long, message: String) = {
    Future {
      Thread.sleep(sleep); throw new RuntimeException(message)
    }
  }



  val f = getFutureWithException(1000, "my exception")

  def recoverFuture(f: Future[Any]) = f.recover {
    case e: RuntimeException => println(e.getMessage)
    case NonFatal(ex) => println("Non Runtime Exceptiont");  Failure(ex)
  }

  var catchError = recoverFuture(f)
  catchError.foreach(c => println(c))

  catchError = recoverFuture(Future{Thread.sleep(500); throw new InterruptedException("my interrupt exception")})
//  catchError = recoverFuture(Future{Thread.sleep(500); throw new IllegalArgumentException("illegal argument")})
  catchError.foreach(c => println(c))



  Thread.sleep(2000)
}
