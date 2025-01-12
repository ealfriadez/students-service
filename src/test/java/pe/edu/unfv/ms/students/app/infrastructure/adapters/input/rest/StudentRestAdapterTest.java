package pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pe.edu.unfv.ms.students.app.application.ports.input.StudentInputPort;
import pe.edu.unfv.ms.students.app.domain.exceptions.StudentNotFoundException;
import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.mapper.StudentRestMapper;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.response.StudentResponse;
import pe.edu.unfv.ms.students.app.utils.TestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(content().json(objectMapper.writeValueAsString(studentResponse)));

        verify(inputPort, times(1)).findById(1L);
        verify(restMapper, times(1)).toStudentResponse(student);
    }

    /*
    @Test
    void findAll() {

    }

    @Test
    void findByIds() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
     */
}