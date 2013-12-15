package by.bsuir.labs.springapp;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Repository("studentDao")
@Transactional(propagation = Propagation.REQUIRED)
public class StudentDao {
    private static final String SELECT_QUERY = "select s from Student s";

    @PersistenceContext
    private EntityManager em;

    public List<Student> findAll() {
        Query query = em.createQuery(SELECT_QUERY);
        List<Student> students = (List<Student>) query.getResultList();
        return students;
    }

    public void save(Student student) {
        em.persist(student);
        em.flush();
    }

    public void delete(Student student) {
        em.remove(student);
        em.flush();
    }

    public Student findOne(Long studentId) {
        Student student = em.find(Student.class, studentId);
        return student;
    }

    public void delete(Long studentId) {
        Student student = em.find(Student.class, studentId);
        em.remove(student);
        em.flush();
    }
}
