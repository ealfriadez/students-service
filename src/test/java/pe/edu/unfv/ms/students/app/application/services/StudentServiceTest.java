package pe.edu.unfv.ms.students.app.application.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unfv.ms.students.app.application.ports.output.ExternalCoursesOutputPort;
import pe.edu.unfv.ms.students.app.application.ports.output.StudentPersistencePort;
import pe.edu.unfv.ms.students.app.domain.exceptions.StudentEmailAlreadyExistsException;
import pe.edu.unfv.ms.students.app.domain.exceptions.StudentNotFoundException;
import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.utils.TestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentPersistencePort studentPersistencePort;

    @Mock
    private ExternalCoursesOutputPort externalCoursesOutputPort;

    @InjectMocks
    private StudentService studentService;

    @Test
    void findById_Success() {
        //Inicializacion
        when(studentPersistencePort.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(TestUtils.buildStudentMock()));

        //Evaluacion del comportamiento
        Student student = studentService.findById(1L);

         //Comprobaciones o aserciones
        assertNotNull(student);
        assertEquals(1L, student.getId());
        assertEquals("Pepito", student.getFirstname());

        verify(studentPersistencePort, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        //Inicializacion
        when(studentPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());

        //Evaluacion del comportamiento

        //Comprobaciones o aserciones
        assertThrows(StudentNotFoundException.class, () -> studentService.findById(1L));

        verify(studentPersistencePort, times(1)).findById(1L);
    }


    @Test
    void findByIds_Success() {
        //Inicializacion
        List<Long> ids = Collections.singletonList(1L);
        when(studentPersistencePort.findByIds(anyCollection()))
                .thenReturn(Collections.singletonList(TestUtils.buildStudentMock()));

        //Evaluacion del comportamiento
        List<Student> students = studentService.findByIds(ids);

        //Comprobaciones o aserciones
        assertNotNull(students);
        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        assertEquals(1L, students.getFirst().getId());
        assertEquals("Pepito", students.getFirst().getFirstname());

        verify(studentPersistencePort, times(1)).findByIds(ids);
    }

    @Test
    void findAll_Success() {
        //Inicializacion
        when(studentPersistencePort.findAll())
                .thenReturn(Collections.singletonList(TestUtils.buildStudentMock()));

        //Evaluacion del comportamiento
        List<Student> students = studentService.findAll();

        //Comprobaciones o aserciones
        assertNotNull(students);
        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        assertEquals(1L, students.getFirst().getId());
        assertEquals("Pepito", students.getFirst().getFirstname());

        verify(studentPersistencePort, times(1)).findAll();
    }

    @Test
    void save_Success() {
        //Inicializacion
        Student studentToSave = Student.builder()
                .id(1L)
                .firstname("Pepito")
                .lastname("Lopez")
                .age(18)
                .email("ealfriadez@gmail.com")
                .address("Calle 1")
                .build();

        when(studentPersistencePort.existByEmail(anyString()))
                .thenReturn(Boolean.FALSE);

        when(studentPersistencePort.save(any(Student.class)))
                .thenReturn(TestUtils.buildStudentMock());

        //Evaluacion del comportamiento
        Student student = studentService.save(studentToSave);

        //Comprobaciones o aserciones
        assertNotNull(student);
        assertEquals(1L, student.getId());
        assertEquals("Pepito", student.getFirstname());
        assertEquals("Lopez", student.getLastname());
        assertEquals(18, student.getAge());
        assertEquals("ealfriadez@gmail.com", student.getEmail());
        assertEquals("Calle 1", student.getAddress());

        verify(studentPersistencePort, times(1)).existByEmail("ealfriadez@gmail.com");
        verify(studentPersistencePort, times(1)).save(any(Student.class));
    }

    @Test
    void save_NotSuccess() {
        //Inicializacion
        Student studentToSave = Student.builder()
                .id(1L)
                .firstname("Pepito")
                .lastname("Lopez")
                .age(18)
                .email("ealfriadez@gmail.com")
                .address("Calle 1")
                .build();

        when(studentPersistencePort.existByEmail(anyString()))
                .thenReturn(Boolean.TRUE);

        //Evaluacion del comportamiento

        //Comprobaciones o aserciones
        assertThrows(StudentEmailAlreadyExistsException.class, () -> studentService.save(studentToSave));

        verify(studentPersistencePort, times(1)).existByEmail("ealfriadez@gmail.com");
        verify(studentPersistencePort, times(0)).save(any(Student.class));
    }

    @Test
    void update() {
        //Inicializacion
        Student studentToUpdate = Student.builder()
                .id(1L)
                .firstname("Pepito")
                .lastname("Lopez")
                .age(18)
                .email("ealfriadez@gmail.com")
                .address("Calle 1")
                .build();

        when(studentPersistencePort.existByEmail(anyString()))
                .thenReturn(Boolean.FALSE);

        when(studentPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(TestUtils.buildStudentMock()));

        when(studentPersistencePort.save(any(Student.class)))
                .thenReturn(studentToUpdate);

        //Evaluacion del comportamiento
        Student student = studentService.update(1L, studentToUpdate);

        //Comprobaciones o aserciones
        assertNotNull(student);
        assertEquals(1L, student.getId());
        assertEquals("Pepito", student.getFirstname());
        assertEquals("Lopez", student.getLastname());
        assertEquals(18, student.getAge());
        assertEquals("ealfriadez@gmail.com", student.getEmail());
        assertEquals("Calle 1", student.getAddress());

        verify(studentPersistencePort, times(1)).existByEmail("ealfriadez@gmail.com");
        verify(studentPersistencePort, times(1)).findById(1L);
        verify(studentPersistencePort, times(1)).save(any(Student.class));
    }

    @Test
    void update_FailedByAlreadyExistsEmail() {
        //Inicializacion
        Student studentToUpdate = Student.builder()
                .id(1L)
                .firstname("Pepito")
                .lastname("Lopez")
                .age(18)
                .email("ealfriadez@gmail.com")
                .address("Calle 1")
                .build();

        when(studentPersistencePort.existByEmail(anyString()))
                .thenReturn(Boolean.TRUE);

        //Evaluacion del comportamiento

        //Comprobaciones o aserciones
        assertThrows(StudentEmailAlreadyExistsException.class, () -> studentService.update(1L, studentToUpdate));

        verify(studentPersistencePort, times(1)).existByEmail("ealfriadez@gmail.com");
        verify(studentPersistencePort, times(0)).findById(1L);
        verify(studentPersistencePort, times(0)).save(studentToUpdate);
    }

    @Test
    void update_FailedByStudentNotFound() {
        //Inicializacion
        Student studentToUpdate = Student.builder()
                .id(1L)
                .firstname("Pepito")
                .lastname("Lopez")
                .age(18)
                .email("ealfriadez@gmail.com")
                .address("Calle 1")
                .build();

        when(studentPersistencePort.existByEmail(anyString()))
                .thenReturn(Boolean.FALSE);

        when(studentPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());

        //Evaluacion del comportamiento

        //Comprobaciones o aserciones
        assertThrows(StudentNotFoundException.class, () -> studentService.update(1L, studentToUpdate));

        verify(studentPersistencePort, times(1)).existByEmail("ealfriadez@gmail.com");
        verify(studentPersistencePort, times(1)).findById(1L);
        verify(studentPersistencePort, times(0)).save(studentToUpdate);
    }

    @Test
    void deleteById_StudentSuccess() {
        //Inicializacion
        when(studentPersistencePort.findById(anyLong()))
                .thenReturn(Optional.of(TestUtils.buildStudentMock()));

        //Evaluacion del comportamiento
       studentService.deleteById(anyLong());

        //Comprobaciones o aserciones
        verify(studentPersistencePort, times(1)).findById(anyLong());
        verify(studentPersistencePort, times(1)).deleteById(anyLong());
        verify(externalCoursesOutputPort, times(1)).remoteStudentFromCollection(anyLong());
    }

    @Test
    void deleteById_StudentNotFoundException() {
        //Inicializacion
        when(studentPersistencePort.findById(anyLong()))
                .thenReturn(Optional.empty());

        //Evaluacion del comportamiento

        //Comprobaciones o aserciones
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteById(anyLong()));

        verify(studentPersistencePort, times(1)).findById(anyLong());
        verify(studentPersistencePort, times(0)).deleteById(anyLong());
        verify(externalCoursesOutputPort, times(0)).remoteStudentFromCollection(anyLong());
    }
}