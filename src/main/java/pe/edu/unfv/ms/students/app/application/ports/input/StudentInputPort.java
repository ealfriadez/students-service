package pe.edu.unfv.ms.students.app.application.ports.input;

import pe.edu.unfv.ms.students.app.domain.models.Student;

import java.util.List;

public interface StudentInputPort {

    Student findById(Long id);
    List<Student> findAll();
    Student save(Student student);
    Student update(Long id, Student student);
    void deleteById(Long id);
}