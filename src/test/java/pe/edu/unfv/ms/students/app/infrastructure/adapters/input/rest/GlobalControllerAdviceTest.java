package pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pe.edu.unfv.ms.students.app.application.ports.input.StudentInputPort;
import pe.edu.unfv.ms.students.app.domain.exceptions.StudentEmailAlreadyExistsException;
import pe.edu.unfv.ms.students.app.domain.exceptions.StudentNotFoundException;
import pe.edu.unfv.ms.students.app.domain.models.Student;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.mapper.StudentRestMapper;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.request.StudentCreateRequest;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.response.ErrorResponse;
import pe.edu.unfv.ms.students.app.utils.TestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.enums.ErrorType.SYSTEM;
import static pe.edu.unfv.ms.students.app.infrastructure.utils.ErrorCatalog.*;

@WebMvcTest(controllers = {StudentRestAdapter.class})
class GlobalControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentInputPort inputPort;

    @MockitoBean
    private StudentRestMapper restMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenThrowsStudentNotFoundException_thenReturNotFound() throws Exception{

        //Inicializacion
        when(inputPort.findById(anyLong()))
                .thenThrow(new StudentNotFoundException());

        //Evaluacion del comportamiento
        ResultActions resultActions = mockMvc.perform(get("/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        //Comprobaciones o aserciones
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    ErrorResponse errorResponse = objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class
                    );
                    assertAll(
                            ()-> assertEquals(STUDENT_NOT_FOUND.getCode(), errorResponse.getCode()),
                            ()-> assertEquals(FUNCTIONAL, errorResponse.getType()),
                            ()-> assertEquals(STUDENT_NOT_FOUND.getMessage(), errorResponse.getMessage()),
                            ()-> assertNotNull(errorResponse.getTimestamp())
                    );
                })
                .andDo(print());

        verify(inputPort, times(1)).findById(1L);
    }

    @Test
    void whenThrowsMethodArgumentNotValidException_thenReturnBadRequest() throws Exception{

        //Inicializacion
        StudentCreateRequest invalidRequest = StudentCreateRequest.builder()
                .lastname("Lopez")
                .age(18)
                .email("pepito@gmail.com")
                .address("Calle 1")
                .build();

        //Evaluacion del comportamiento
        ResultActions resultActions = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)));

        //Comprobaciones o aserciones
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    ErrorResponse errorResponse = objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class
                    );
                    assertAll(
                            ()-> assertEquals(STUDENT_BAD_PARAMETERS.getCode(), errorResponse.getCode()),
                            ()-> assertEquals(FUNCTIONAL, errorResponse.getType()),
                            ()-> assertEquals(STUDENT_BAD_PARAMETERS.getMessage(), errorResponse.getMessage()),
                            ()-> assertEquals(1, errorResponse.getDetails().size()),
                            ()-> assertNotNull(errorResponse.getTimestamp())
                    );
                })
                .andDo(print());
    }

    @Test
    void whenThrowsStudentEmailAlreadyExistsException_thenReturnBadRequest() throws Exception{
        //Inicializacion
        StudentCreateRequest updateRequest = TestUtils.buildStudentCreateRequestMock();
        Student student = TestUtils.buildStudentMock();

        when(restMapper.toStudent(any(StudentCreateRequest.class)))
                .thenReturn(student);

        when(inputPort.update(anyLong(), any(Student.class)))
                .thenThrow(new StudentEmailAlreadyExistsException(updateRequest.getEmail()));

        //Evaluacion del comportamiento
        ResultActions resultActions = mockMvc.perform(put("/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        //Comprobaciones o aserciones
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    ErrorResponse errorResponse = objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class
                    );
                    assertAll(
                            ()-> assertEquals(STUDENT_EMAIL_ALREADY_EXISTS.getCode(), errorResponse.getCode()),
                            ()-> assertEquals(FUNCTIONAL, errorResponse.getType()),
                            ()-> assertEquals(STUDENT_EMAIL_ALREADY_EXISTS.getMessage(), errorResponse.getMessage()),
                            ()-> assertEquals("Student email: " + updateRequest.getEmail() + " already exists!", errorResponse.getDetails().get(0)),
                            ()-> assertNotNull(errorResponse.getTimestamp())
                    );
                })
                .andDo(print());
    }

    @Test
    void whenThrowGenericException_thenReturnInternalServerError() throws Exception{

        //Inicializacion
        when(inputPort.findAll())
                .thenThrow(new RuntimeException("Generic error"));

        //Evaluacion del comportamiento
        ResultActions resultActions = mockMvc.perform(get("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //Comprobaciones o aserciones
        resultActions
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    ErrorResponse errorResponse = objectMapper.readValue(
                            result.getResponse().getContentAsString(), ErrorResponse.class
                    );
                    assertThat(errorResponse.getCode()).isEqualTo(INTERNAL_SERVER_ERROR.getCode());
                    assertThat(errorResponse.getType()).isEqualTo(SYSTEM);
                    assertThat(errorResponse.getMessage()).isEqualTo(INTERNAL_SERVER_ERROR.getMessage());
                    assertThat(errorResponse.getDetails().get(0)).isEqualTo("Generic error");
                    assertThat(errorResponse.getTimestamp()).isNotNull();
                })
                .andDo(print());
    }
}