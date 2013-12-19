package by.bsuir.labs.scala

object TopStudent {

  case class Student(name: String, score: Int)

  def max(s1: Student, s2: Student): Student = if (s1.score > s2.score) s1 else s2

  def topStud(studs: Seq[Student]): Student = studs.reduceLeft(max)


  def main(args: Array[String]) {
    val topStudent = topStud(Seq(Student("S1", 83), Student("S2", 80), Student("S3", 85), Student("S4", 90), Student("S5", 95)))
    println(topStudent)
  }
}