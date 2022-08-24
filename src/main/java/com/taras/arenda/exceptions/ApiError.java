package com.taras.arenda.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private HttpStatus status;
    private String message;
    private Map<String, String> errors;

}
