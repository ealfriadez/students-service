package pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.request.StudentCreateRequest;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.response.StudentResponse;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StudentRestMapper {

    //@Mapping(target = "id", ignore = true)
    Student toStudent(StudentCreateRequest request);

    @Mapping(target = "timestamp", expression = "java(mapTimestamp())")
    StudentResponse toStudentResponse(Student student);

    List<StudentResponse> toStudentResponses(List<Student> students);

    default String mapTimestamp(){
        return LocalDate.now().toString();
    }
}