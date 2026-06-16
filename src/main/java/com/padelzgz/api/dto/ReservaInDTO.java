package com.padelzgz.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

// DTO para crear/modificar reserva (incluye IDs de relaciones)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaInDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private String horaInicio; // HH:mm

    @NotNull(message = "La hora de fin es obligatoria")
    private String horaFin;

    private float precio;
    private boolean pagado;

    @Size(max = 500)
    private String comentario;

    @NotNull(message = "El id de la pista es obligatorio")
    private Long pistaId;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;
}
