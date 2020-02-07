object Sequences extends App {

  private val s: Seq[Int] = Seq(1, 2, 3)


  private val sum: Int = s.foldLeft(0)(_ + _)
  private val sum2: Int = s.foldRight(0)(_ + _)

  println(sum)
  println(sum2)

  println(s.foldLeft("")((a, b) => s"$a $b"))
  println(s.foldRight("")((a, b) => s"$a $b"))
}
