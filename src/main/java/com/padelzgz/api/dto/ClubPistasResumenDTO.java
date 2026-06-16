package com.padelzgz.api.dto;

import lombok.*;
import java.util.List;

// DTO de respuesta: resumen de pistas de un club con su valoración media
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubPistasResumenDTO {

    private long clubId;
    private String nombreClub;
    private String ciudad;
    private int totalPistas;
    private int pistasActivas;
    private List<PistaResumenDTO> pistas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PistaResumenDTO {
        private long id;
        private int numero;
        private String tipo;
        private boolean interior;
        private float precioHora;
        private double valoracionMedia;
    }
}
