package pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.unfv.ms.students.app.domain.exceptions.StudentEmailAlreadyExistsException;
import pe.edu.unfv.ms.students.app.domain.exceptions.StudentNotFoundException;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.enums.ErrorType;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.response.ErrorResponse;

import java.time.LocalDate;
import java.util.Collections;

import static pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.enums.ErrorType.FUNCTIONAL;
import static pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.enums.ErrorType.SYSTEM;
import static pe.edu.unfv.ms.students.app.infrastructure.utils.ErrorCatalog.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    private final String ERROR_LOG_MESSAGE = "Error -> code: {}, message: {}";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StudentNotFoundException.class)
    public ErrorResponse handleStudentNotFoundException(){

        log.error(ERROR_LOG_MESSAGE, STUDENT_NOT_FOUND.getCode(), STUDENT_NOT_FOUND.getMessage());

        return ErrorResponse.builder()
                .code(STUDENT_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(STUDENT_NOT_FOUND.getMessage())
                .timestamp(LocalDate.now().toString())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        log.error(ERROR_LOG_MESSAGE, STUDENT_BAD_PARAMETERS.getCode(), STUDENT_BAD_PARAMETERS.getMessage());

        BindingResult bindingResult = e.getBindingResult();
        return ErrorResponse .builder()
                .code(STUDENT_BAD_PARAMETERS.getCode())
                .type(FUNCTIONAL)
                .message(STUDENT_BAD_PARAMETERS.getMessage())
                .details(bindingResult.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList())
                .timestamp(LocalDate.now().toString())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StudentEmailAlreadyExistsException.class)
    public ErrorResponse handleStudentEmailAlreadyExistsException(StudentEmailAlreadyExistsException e){

        log.error(ERROR_LOG_MESSAGE, STUDENT_EMAIL_ALREADY_EXISTS.getCode(), STUDENT_EMAIL_ALREADY_EXISTS.getMessage());

        return ErrorResponse.builder()
                .code(STUDENT_EMAIL_ALREADY_EXISTS.getCode())
                .type(FUNCTIONAL)
                .message(STUDENT_EMAIL_ALREADY_EXISTS.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e){

        log.error(ERROR_LOG_MESSAGE, INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage());

        return ErrorResponse.builder()
                .code(INTERNAL_SERVER_ERROR.getCode())
                .type(SYSTEM)
                .message(INTERNAL_SERVER_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDate.now().toString())
                .build();
    }
}
