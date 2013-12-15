package by.bsuir.labs.springapp;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "grades")
@AssociationOverrides({
        @AssociationOverride(name = "pk.student",
                joinColumns = @JoinColumn(name = "id_student")),
        @AssociationOverride(name = "pk.course",
                joinColumns = @JoinColumn(name = "id_course")) })
public class Grade implements Serializable{

    @EmbeddedId
    private StudentCourseId pk;

    public Grade() {
    }

    public StudentCourseId getPk() {
        return pk;
    }

    public void setPk(StudentCourseId pk) {
        this.pk = pk;
    }

    @Transient
    public Student getStudent() {
        return getPk().getStudent();
    }

    public void setStudent(Student student) {
        getPk().setStudent(student);
    }

    @Transient
    public Course getCourse() {
        return getPk().getCourse();
    }

    public void setCourse(Course course) {
        getPk().setCourse(course);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade = (Grade) o;

        if (!pk.equals(grade.pk)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pk.hashCode();
    }

    @Override
    public String toString() {
        return "Grade{" +
                "pk=" + pk +
                '}';
    }

    @Embeddable
    public static class StudentCourseId implements Serializable{

        private Student student;
        private Course course;

        @ManyToOne
        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        @ManyToOne
        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StudentCourseId that = (StudentCourseId) o;

            if (!course.equals(that.course)) return false;
            if (!student.equals(that.student)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = student.hashCode();
            result = 31 * result + course.hashCode();
            return result;
        }
    }
}
