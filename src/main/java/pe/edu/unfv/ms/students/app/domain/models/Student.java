package pe.edu.unfv.ms.students.app.domain.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private Long id;
    private String firstname;
    private String lastname;
    private Integer age;
    private String email;
    private String address;
}
