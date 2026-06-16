package com.padelzgz.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"usuario", "torneo"})
@ToString(exclude = {"usuario", "torneo"})
@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fecha_inscripcion", nullable = false)
    @NotNull(message = "La fecha de inscripción es obligatoria")
    private LocalDate fechaInscripcion;

    @Column
    private String estado;

    @Column
    private boolean pagado;

    @Column(name = "numero_pareja")
    private int numeroPareja;

    @Column
    @Size(max = 500)
    private String notas;

    @Column(name = "fecha_confirmacion")
    private LocalDate fechaConfirmacion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario_inscripciones")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "torneo_id", nullable = false)
    @JsonBackReference(value = "torneo_inscripciones")
    private Torneo torneo;
}
