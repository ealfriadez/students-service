package pe.edu.unfv.ms.students.app.infrastructure.adapters.output.restclient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.restclient.client.CourseFeignClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseRestClientAdapterTest {

    @Mock
    private CourseFeignClient courseFeignClient;

    @InjectMocks
    private CourseRestClientAdapter restClientAdapter;

    @Test
    void remoteStudentFromCollection() {
        //Inicializacion
        doNothing().when(courseFeignClient).removeStudentFromCollection(anyLong());

        //Evaluacion del comportamiento
        restClientAdapter.remoteStudentFromCollection(1L);

        //Comprobaciones o aserciones
        verify(courseFeignClient, times(1)).removeStudentFromCollection(anyLong());
    }
}