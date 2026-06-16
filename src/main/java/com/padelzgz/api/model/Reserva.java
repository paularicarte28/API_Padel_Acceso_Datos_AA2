package com.padelzgz.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"pista", "usuario"})
@ToString(exclude = {"pista", "usuario"})
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @Column
    private float precio;

    @Column
    private boolean pagado;

    @Column
    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "pista_id", nullable = false)
    @JsonBackReference(value = "pista_reservas")
    private Pista pista;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario_reservas")
    private Usuario usuario;
}
