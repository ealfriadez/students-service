package pe.edu.unfv.ms.students.app.domain.exceptions;

public class StudentEmailAlreadyExistsException extends RuntimeException{

    public StudentEmailAlreadyExistsException(String email) {
        super("Student email: ".concat(email).concat(" already exists!"));
    }
}
