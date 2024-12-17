package pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.models.StudentEntity;

public interface StudentJpaRepository extends CrudRepository<StudentEntity, Long> {
}
