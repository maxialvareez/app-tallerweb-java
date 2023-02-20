package com.tallerweb.apptallerwebjava.Util.dto;

import com.tallerweb.apptallerwebjava.models.User;

public class ItemDTO {

    private String id;
    private String nombre;
    private String descripcion;
    private Double costo;
    private Boolean pago;
    private User creadoPor;

    public ItemDTO() {
    }

    public ItemDTO(String nombre, String descripcion, Double costo, boolean pago, User creadoPor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.pago = pago;
        this.creadoPor = creadoPor;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }
    
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getCosto() {
        return this.costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Boolean isPago() {
        return this.pago;
    }

    public Boolean getPago() {
        return this.pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public User getCreadoPor() {
        return this.creadoPor;
    }

    public void setCreadoPor(User creadoPor) {
        this.creadoPor = creadoPor;
    }
    
}
