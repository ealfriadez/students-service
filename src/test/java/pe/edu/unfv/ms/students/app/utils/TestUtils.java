package pe.edu.unfv.ms.students.app.utils;

import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.response.StudentResponse;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.models.StudentEntity;

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
                .build();
    }
}
