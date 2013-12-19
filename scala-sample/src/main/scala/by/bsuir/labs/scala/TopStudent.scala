package by.bsuir.labs.scala

object TopStudent {

  case class Student(name: String, score: Int)

  def max(s1: Student, s2: Student): Student = if (s1.score > s2.score) s1 else s2

  def topStud(studs: Seq[Student]): Student = studs.reduceLeft(max)


  def main(args: Array[String]) {
    val alex = Student("S1", 83)
    val david = Student("S2", 80)
    val frank = Student("S3", 85)
    val julia = Student("S4", 90)
    val kim = Student("S5", 95)

    val topStudent = topStud(Seq(alex, david, frank, julia, kim))

    println(topStudent)

  }
}