package com.padelzgz.api.model;

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
@EqualsAndHashCode(exclude = {"pistas", "torneos"})
@ToString(exclude = {"pistas", "torneos"})
@Entity
@Table(name = "clubs")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @Column
    private String direccion;

    @Column
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @Column
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Column
    private boolean activo;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "club_pistas")
    private Set<Pista> pistas;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "club_torneos")
    private Set<Torneo> torneos;
}
