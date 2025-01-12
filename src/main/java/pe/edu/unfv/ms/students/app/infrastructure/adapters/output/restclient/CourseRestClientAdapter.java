package pe.edu.unfv.ms.students.app.infrastructure.adapters.output.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.unfv.ms.students.app.application.ports.output.ExternalCoursesOutputPort;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.restclient.client.CourseFeignClient;

@Component
@RequiredArgsConstructor
public class CourseRestClientAdapter implements ExternalCoursesOutputPort {

    private final CourseFeignClient client;

    @Override
    public void remoteStudentFromCollection(Long studentId) {
        client.removeStudentFromCollection(studentId);
    }
}
