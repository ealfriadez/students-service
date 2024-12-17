package pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.unfv.ms.students.app.application.ports.output.StudentPersistencePort;
import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.mapper.StudentPersistenceMapper;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.models.StudentEntity;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.repository.StudentJpaRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StudentPersistenceAdapter implements StudentPersistencePort {

    private final StudentJpaRepository jpaRepository;
    private final StudentPersistenceMapper mapper;

    @Override
    public Optional<Student> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toStudent);
    }

    @Override
    public List<Student> findAll() {
        return mapper.toStudents((List<StudentEntity>) jpaRepository.findAll());
    }

    @Override
    public Student save(Student student) {
        return mapper.toStudent(
                jpaRepository.save(mapper.toStudentEntity(student)));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
