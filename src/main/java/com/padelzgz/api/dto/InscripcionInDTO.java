package com.padelzgz.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

// DTO entrada Inscripcion
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionInDTO {

    @NotNull(message = "La fecha de inscripción es obligatoria")
    private LocalDate fechaInscripcion;

    private String estado = "PENDIENTE";
    private boolean pagado;
    private int numeroPareja;

    @Size(max = 500)
    private String notas;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El id del torneo es obligatorio")
    private Long torneoId;
}
