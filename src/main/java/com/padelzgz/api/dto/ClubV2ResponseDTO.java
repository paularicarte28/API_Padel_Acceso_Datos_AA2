package com.padelzgz.api.dto;

import java.time.LocalDate;

public class ClubV2ResponseDTO {

    private long id;
    private String nombre;
    private String ciudad;
    private String direccion;
    private String telefono;
    private String email;
    private LocalDate fechaApertura;
    private boolean activo;
    private long totalPistas;
    private long pistasActivas;

    public ClubV2ResponseDTO() {
    }

    public ClubV2ResponseDTO(long id, String nombre, String ciudad, String direccion, String telefono,
                             String email, LocalDate fechaApertura, boolean activo,
                             long totalPistas, long pistasActivas) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fechaApertura = fechaApertura;
        this.activo = activo;
        this.totalPistas = totalPistas;
        this.pistasActivas = pistasActivas;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public boolean isActivo() {
        return activo;
    }

    public long getTotalPistas() {
        return totalPistas;
    }

    public long getPistasActivas() {
        return pistasActivas;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setTotalPistas(long totalPistas) {
        this.totalPistas = totalPistas;
    }

    public void setPistasActivas(long pistasActivas) {
        this.pistasActivas = pistasActivas;
    }
}