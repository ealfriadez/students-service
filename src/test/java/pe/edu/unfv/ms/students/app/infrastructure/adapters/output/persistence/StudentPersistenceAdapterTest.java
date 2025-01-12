package pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.mapper.StudentPersistenceMapper;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.models.StudentEntity;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.repository.StudentJpaRepository;
import pe.edu.unfv.ms.students.app.utils.TestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentPersistenceAdapterTest {

    @Mock
    private StudentJpaRepository studentJpaRepository;

    @Mock
    private StudentPersistenceMapper studentPersistenceMapper;

    @InjectMocks
    private StudentPersistenceAdapter studentPersistenceAdapter;

    @Test
    void findById() {
        //Inicializacion
        Student studentExpected = TestUtils.buildStudentMock();
        StudentEntity entity = TestUtils.buildStudentEntityMock();

        when(studentJpaRepository.findById(anyLong()))
                .thenReturn(Optional.of(entity));

        when(studentPersistenceMapper.toStudent(any(StudentEntity.class)))
                .thenReturn(studentExpected);

        //Evaluacion del comportamiento
        Optional<Student> studentFounded = studentPersistenceAdapter.findById(1L);

        //Comprobaciones o aserciones
        assertAll(
                () -> assertTrue(studentFounded.isPresent()),
                () -> assertEquals(studentExpected, studentFounded.get()),
                () -> assertEquals(1L, studentFounded.get().getId()),
                () -> assertEquals("Pepito", studentFounded.get().getFirstname())
        );

        verify(studentJpaRepository, times(1)).findById(1L);
        verify(studentPersistenceMapper, times(1)).toStudent(any(StudentEntity.class));
    }

    @Test
    void findById_notFound() {
        //Inicializacion
        when(studentJpaRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //Evaluacion del comportamiento
        Optional<Student> studentFounded = studentPersistenceAdapter.findById(1L);

        //Comprobaciones o aserciones
        assertAll(
                () -> assertFalse(studentFounded.isPresent()),
                () -> assertTrue(studentFounded.isEmpty())
        );

        verify(studentJpaRepository, times(1)).findById(1L);
        verify(studentPersistenceMapper, times(0)).toStudent(any(StudentEntity.class));
    }

    @Test
    void findByIds() {
        //Inicializacion
        List<StudentEntity> entities = Collections.singletonList(TestUtils.buildStudentEntityMock());
        List<Student> students = Collections.singletonList(TestUtils.buildStudentMock());

        when(studentJpaRepository.findAllById(anyList()))
                .thenReturn(entities);

        when(studentPersistenceMapper.toStudents(anyList()))
                .thenReturn(students);

        //Evaluacion del comportamiento
        List<Student> studentFounded = studentPersistenceAdapter.findByIds(Collections.singletonList(1L));

        //Comprobaciones o aserciones
        assertAll(
                () -> assertFalse(studentFounded.isEmpty()),
                () -> assertEquals(1, studentFounded.size())
        );

        verify(studentJpaRepository, times(1)).findAllById(anyList());
        verify(studentPersistenceMapper, times(1)).toStudents(anyList());
    }

    @Test
    void findAll() {
        //Inicializacion
        List<StudentEntity> entities = Collections.singletonList(TestUtils.buildStudentEntityMock());
        List<Student> students = Collections.singletonList(TestUtils.buildStudentMock());

        when(studentJpaRepository.findAll())
                .thenReturn(entities);

        when(studentPersistenceMapper.toStudents(anyList()))
                .thenReturn(students);

        //Evaluacion del comportamiento
        List<Student> studentFounded = studentPersistenceAdapter.findAll();

        //Comprobaciones o aserciones
        assertAll(
                () -> assertFalse(studentFounded.isEmpty()),
                () -> assertEquals(1, studentFounded.size())
        );

        verify(studentJpaRepository, times(1)).findAll();
        verify(studentPersistenceMapper, times(1)).toStudents(anyList());
    }

    @Test
    void existByEmail_True() {
        //Inicializacion
        when(studentJpaRepository.existsByEmailIgnoreCase(anyString()))
                .thenReturn(true);

        //Evaluacion del comportamiento
        boolean result = studentPersistenceAdapter.existByEmail("ealfriadez@gmail.com");

        //Comprobaciones o aserciones
        assertAll(
                () -> assertTrue(result)
        );

        verify(studentJpaRepository, times(1)).existsByEmailIgnoreCase(anyString());
    }

    @Test
    void existByEmail_False() {
        //Inicializacion
        when(studentJpaRepository.existsByEmailIgnoreCase(anyString()))
                .thenReturn(false);

        //Evaluacion del comportamiento
        boolean result = studentPersistenceAdapter.existByEmail("ealfriadez@gmail.com");

        //Comprobaciones o aserciones
        assertAll(
                () -> assertFalse(result)
        );

        verify(studentJpaRepository, times(1)).existsByEmailIgnoreCase(anyString());
    }

    @Test
    void save() {
        //Inicializacion
        Student studentExpected = TestUtils.buildStudentMock();
        StudentEntity entity = TestUtils.buildStudentEntityMock();

        when(studentPersistenceMapper.toStudentEntity(any(Student.class)))
                .thenReturn(entity);

        when(studentJpaRepository.save(any(StudentEntity.class)))
                .thenReturn(entity);

        when(studentPersistenceMapper.toStudent(any(StudentEntity.class)))
                .thenReturn(studentExpected);

        //Evaluacion del comportamiento
        Student studenSave = studentPersistenceAdapter.save(studentExpected);

        //Comprobaciones o aserciones
        assertNotNull(studenSave);
        assertEquals(studentExpected, studenSave);

        verify(studentPersistenceMapper, times(1)).toStudentEntity(any(Student.class));
        verify(studentJpaRepository, times(1)).save(entity);
        verify(studentPersistenceMapper, times(1)).toStudent(any(StudentEntity.class));
    }

    @Test
    void deleteById() {
        //Inicializacion
        doNothing().when(studentJpaRepository).deleteById(anyLong());

        //Evaluacion del comportamiento
        studentPersistenceAdapter.deleteById(1L);

        //Comprobaciones o aserciones
        verify(studentJpaRepository, times(1)).deleteById(anyLong());
    }
}