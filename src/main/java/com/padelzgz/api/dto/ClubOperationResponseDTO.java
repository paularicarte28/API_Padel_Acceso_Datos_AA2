package com.padelzgz.api.dto;

public class ClubOperationResponseDTO {

    private String mensaje;
    private ClubV2ResponseDTO club;

    public ClubOperationResponseDTO() {
    }

    public ClubOperationResponseDTO(String mensaje, ClubV2ResponseDTO club) {
        this.mensaje = mensaje;
        this.club = club;
    }

    public String getMensaje() {
        return mensaje;
    }

    public ClubV2ResponseDTO getClub() {
        return club;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setClub(ClubV2ResponseDTO club) {
        this.club = club;
    }
}