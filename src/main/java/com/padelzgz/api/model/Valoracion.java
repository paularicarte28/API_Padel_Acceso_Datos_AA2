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
@EqualsAndHashCode(exclude = {"pista", "usuario"})
@ToString(exclude = {"pista", "usuario"})
@Entity
@Table(name = "valoraciones")
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private int puntuacion;

    @Column
    @Size(max = 1000)
    private String comentario;

    @Column(name = "fecha_valoracion", nullable = false)
    @NotNull
    private LocalDate fechaValoracion;

    @Column
    private boolean verificada;

    @Column(name = "util_count")
    private int utilCount;

    @Column
    private String visibilidad;

    @ManyToOne
    @JoinColumn(name = "pista_id", nullable = false)
    @JsonBackReference(value = "pista_valoraciones")
    private Pista pista;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario_valoraciones")
    private Usuario usuario;
}
