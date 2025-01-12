package pe.edu.unfv.ms.students.app.infrastructure.adapters.input.rest.models.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateRequest {

    @NotBlank(message = "Field firstname cannot be null or blank")
    private String firstname;

    @NotBlank(message = "Field lastname cannot be null or blank")
    private String lastname;

    @NotNull(message = "Field age cannot be null")
    @Min(value = 1, message = "Field age must be greater than zero")
    private Integer age;

    @NotBlank(message = "Field address cannot be null or blank")
    private String address;
}
