package com.padelzgz.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"club", "inscripciones"})
@ToString(exclude = {"club", "inscripciones"})
@Entity
@Table(name = "torneos")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column
    @Size(max = 1000)
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "max_participantes")
    @Min(value = 2, message = "Debe haber al menos 2 participantes")
    private int maxParticipantes;

    @Column(name = "inscripcion_abierta")
    private boolean inscripcionAbierta;

    @Column(name = "precio_inscripcion")
    private float precioInscripcion;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @JsonBackReference(value = "club_torneos")
    private Club club;

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "torneo_inscripciones")
    private Set<Inscripcion> inscripciones;
}
