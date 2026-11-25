package co.edu.ufps.prestamos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PrestamoRequest(
        @NotNull Long libroId,
        @NotBlank String codigoEstudiante) {
}
