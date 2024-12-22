package pe.edu.unfv.ms.students.app.application.ports.output;

import pe.edu.unfv.ms.students.app.domain.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentPersistencePort {

    Optional<Student> findById(Long id);
    List<Student> findByIds(Iterable<Long> ids);
    List<Student> findAll();
    boolean existByEmail(String email);
    Student save(Student student);
    void deleteById(Long id);
}
