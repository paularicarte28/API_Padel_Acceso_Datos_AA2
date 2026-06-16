package com.padelzgz.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"club", "reservas", "valoraciones"})
@ToString(exclude = {"club", "reservas", "valoraciones"})
@Entity
@Table(name = "pistas")
public class Pista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotNull(message = "El número de pista es obligatorio")
    @Min(value = 1, message = "El número de pista debe ser mayor que 0")
    private int numero;

    @Column(nullable = false)
    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @Column
    private boolean interior;

    @Column(name = "precio_hora")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private float precioHora;

    @Column
    private boolean activa;

    @Column
    private String superficie;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @JsonBackReference(value = "club_pistas")
    private Club club;

    @OneToMany(mappedBy = "pista", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "pista_reservas")
    private Set<Reserva> reservas;

    @OneToMany(mappedBy = "pista", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "pista_valoraciones")
    private Set<Valoracion> valoraciones;
}
