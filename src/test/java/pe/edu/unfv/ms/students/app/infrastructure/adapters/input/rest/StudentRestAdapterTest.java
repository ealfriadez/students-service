package pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pe.edu.unfv.ms.students.app.application.ports.input.StudentInputPort;
import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.mapper.StudentRestMapper;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.request.StudentCreateRequest;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.response.StudentResponse;
import pe.edu.unfv.ms.students.app.utils.TestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentRestAdapter.class)
class StudentRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentInputPort inputPort;

    @MockitoBean
    private StudentRestMapper restMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Test find by id when id exists")
    void findById() throws Exception {
        //Inicializacion
        Student student = TestUtils.buildStudentMock();
        StudentResponse studentResponse = TestUtils.buildStudentResponseMock();

        when(inputPort.findById(anyLong()))
                .thenReturn(student);

        when(restMapper.toStudentResponse(any(Student.class)))
                .thenReturn(studentResponse);

        //Evaluacion del comportamiento
        ResultActions resultActions = mockMvc.perform(get("/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        //Comprobaciones o aserciones
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentResponse)))
                .andDo(print());

        verify(inputPort, times(1)).findById(1L);
        verify(restMapper, times(1)).toStudentResponse(student);
    }

    @Test
    void findByIds() throws Exception{

        //Inicializacion
        List<Student> students = Collections.singletonList(TestUtils.buildStudentMock());
        List<StudentResponse> studentsResponse = Collections.singletonList(TestUtils.buildStudentResponseMock());

        when(inputPort.findByIds(anyList()))
                .thenReturn(students);

        when(restMapper.toStudentResponses(anyList()))
                .thenReturn(studentsResponse);

        //Evaluacion del comportamiento
        ResultActions resultActions = mockMvc.perform(get("/students/find-by-ids").param("ids","1")
                .contentType(MediaType.APPLICATION_JSON));

        //Comprobaciones o aserciones
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentsResponse)))
                .andDo(print());

        verify(inputPort, times(1)).findByIds(anyList());
        verify(restMapper, times(1)).toStudentResponses(anyList());
    }

    @Test
    void findAll() throws Exception{

        //Inicializacion
        List<Student> students = Collections.singletonList(TestUtils.buildStudentMock());
        List<StudentResponse> studentsResponse = Collections.singletonList(TestUtils.buildStudentResponseMock());

        when(inputPort.findAll())
                .thenReturn(students);

        when(restMapper.toStudentResponses(anyList()))
                .thenReturn(studentsResponse);

        //Evaluacion del comportamiento
        ResultActions resultActions = mockMvc.perform(get("/students")
                .contentType(MediaType.APPLICATION_JSON));

        //Comprobaciones o aserciones
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentsResponse)))
                .andDo(print());

        verify(inputPort, times(1)).findAll();
        verify(restMapper, times(1)).toStudentResponses(anyList());
    }

    @Test
    void save() throws Exception{

        //Inicializacion
        StudentCreateRequest request = TestUtils.buildStudentCreateRequestMock();
        Student student = TestUtils.buildStudentMock();
        StudentResponse studentResponse = TestUtils.buildStudentResponseMock();

        when(restMapper.toStudent(any(StudentCreateRequest.class)))
                .thenReturn(student);

        when(inputPort.save(any(Student.class)))
                .thenReturn(student);

        when(restMapper.toStudentResponse(any(Student.class)))
                .thenReturn(studentResponse);

        //Evaluacion del comportamiento
        ResultActions resultActions = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        //Comprobaciones o aserciones
        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/students/1"))
                .andExpect(content().json(objectMapper.writeValueAsString(studentResponse)))
                .andDo(print());

        verify(restMapper, times(1)).toStudent(any(StudentCreateRequest.class));
        verify(inputPort, times(1)).save(any(Student.class));
        verify(restMapper, times(1)).toStudentResponse(any(Student.class));
    }

    @Test
    void update() throws Exception {

        //Inicializacion
        StudentCreateRequest request = TestUtils.buildStudentCreateRequestMock();
        Student student = TestUtils.buildStudentMock();
        StudentResponse studentResponse = TestUtils.buildStudentResponseMock();

        when(restMapper.toStudent(any(StudentCreateRequest.class)))
                .thenReturn(student);

        when(inputPort.update(anyLong(), any(Student.class)))
                .thenReturn(student);

        when(restMapper.toStudentResponse(any(Student.class)))
                .thenReturn(studentResponse);

        //Evaluacion del comportamiento
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/students/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        //Comprobaciones o aserciones
        resultActions
                .andExpect(status().isOk())
                .andExpect(result -> {
                    StudentResponse updatedStudent = objectMapper.readValue(
                            result.getResponse().getContentAsString(), StudentResponse.class);

                    assertEquals("Pepito", updatedStudent.getFirstname());
                    assertThat(updatedStudent.getLastname()).isEqualTo("Lopez");

                    assertAll(() -> assertEquals(18, studentResponse.getAge()),
                            () -> assertEquals("ealfriadez@gmail.com", studentResponse.getEmail()),
                            () -> assertEquals("Calle 1", studentResponse.getAddress()),
                            () -> assertEquals(LocalDate.now().toString(), studentResponse.getTimestamp()));

                })
                .andDo(print());

        verify(restMapper, times(1)).toStudent(any(StudentCreateRequest.class));
        verify(inputPort, times(1)).update(anyLong(), any(Student.class));
        verify(restMapper, times(1)).toStudentResponse(any(Student.class));
    }

    @Test
    void deleteById() throws Exception{

        //Inicializacion
        doNothing().when(inputPort).deleteById(anyLong());

        //Evaluacion del comportamiento
        mockMvc.perform(delete("/students//{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //Comprobaciones o aserciones
        verify(inputPort, times(1)).deleteById(anyLong());
    }
}