package pe.edu.unfv.ms.students.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.models.StudentEntity;
import pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.repository.StudentJpaRepository;

import java.util.Arrays;

@SpringBootApplication
@RequiredArgsConstructor
public class StudentsServiceApplication implements CommandLineRunner {

    private final StudentJpaRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(StudentsServiceApplication.class, args);
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {

        repository.saveAll(Arrays.asList(
                new StudentEntity(null, "Santiago", "Nicolas", 11, "santiago@gmail.com", "Calle 1"),
                new StudentEntity(null, "Sebastian", "Alfredo", 22, "sebastian@gmail.com", "Calle 2"),
                new StudentEntity(null, "Doris", "Pilar", 45, "doris@gmail.com", "Calle 3"),
                new StudentEntity(null, "Mocka", "Negra", 5, "mocka@gmail.com", "Calle 4")
        ));
    }
}
