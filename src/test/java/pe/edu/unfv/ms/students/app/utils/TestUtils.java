package pe.edu.unfv.ms.students.app.utils;

import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.request.StudentCreateRequest;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.response.StudentResponse;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.models.StudentEntity;

import java.time.LocalDate;

public class TestUtils {

    public static Student buildStudentMock(){
        return Student.builder()
                .id(1L)
                .firstname("Pepito")
                .lastname("Lopez")
                .age(18)
                .email("ealfriadez@gmail.com")
                .address("Calle 1")
                .build();
    }

    public static StudentEntity buildStudentEntityMock(){
        return StudentEntity.builder()
                .id(1L)
                .firstname("Pepito")
                .lastname("Lopez")
                .age(18)
                .email("ealfriadez@gmail.com")
                .address("Calle 1")
                .build();
    }

    public static StudentResponse buildStudentResponseMock(){
        return StudentResponse.builder()
                .id(1L)
                .firstname("Pepito")
                .lastname("Lopez")
                .age(18)
                .email("ealfriadez@gmail.com")
                .address("Calle 1")
                .timestamp(LocalDate.now().toString())
                .build();
    }

    public static StudentCreateRequest buildStudentCreateRequestMock(){
        return StudentCreateRequest.builder()
                .firstname("Pepito")
                .lastname("Lopez")
                .age(18)
                .email("ealfriadez@gmail.com")
                .address("Calle 1")
                .build();
    }
}
