package pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.models.StudentEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentPersistenceMapper {

    StudentEntity toStudentEntity(Student student);

    Student toStudent(StudentEntity entity);

    List<Student> toStudents(List<StudentEntity> entities);
}
