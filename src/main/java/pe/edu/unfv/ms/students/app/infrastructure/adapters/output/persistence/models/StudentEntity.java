package pe.edu.unfv.ms.students.app.infrastructure.adapters.output.persistence.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
public class StudentEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private Integer age;
    private String email;
    private String address;
}
