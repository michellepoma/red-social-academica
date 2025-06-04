package red_social_academica.red_social_academica.validation;

import java.time.LocalDateTime;

import lombok.*;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private int status;
    private String mensaje;
    private Object detalles;
    private LocalDateTime timestamp;
}
