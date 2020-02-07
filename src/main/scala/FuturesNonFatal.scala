import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}


object FuturesNonFatal extends App {

  // Some exception onComplete can't catch.
  /*
  def getFuture(wait: Long, ex: Exception) = {
    Future { Thread.sleep(wait); throw new ex}
  }*/


  // non fatal exception
  Future{throw new IllegalArgumentException}.failed foreach { case t => println(s"error - $t")}
  Thread.sleep(500)


  // !!!!!!! fatal exception
  //Future{throw new InterruptedException("My fatal exception")}.failed foreach { case t => println(s"error - $t")}
  Thread.sleep(500)

  // catching fatal exception
  Future{throw new InterruptedException("My non fatal exception")}.failed foreach {
    case t => println(s"error - $t")
    case NonFatal(t) => println(s"Non fatal error - ${t.getMessage}")}

  Thread.sleep(500)
  println("Msg after fatal exception")

}
