package com.tallerweb.apptallerwebjava.Util.dto;

import java.util.List;

import com.tallerweb.apptallerwebjava.models.Item;
import com.tallerweb.apptallerwebjava.models.User;

public class GroupDTO {

    private String nombre;
    private String descripcion;
    private boolean estado;
    private User creadoPor;
    private List<User> integrantes;
    private List<Item> items;

    public GroupDTO() {
    }

    public GroupDTO(String nombre, String descripcion, boolean estado, User creadoPor, List<User> integrantes, List<Item> items) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.integrantes = integrantes;
        this.items = items;
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

    public boolean isEstado() {
        return this.estado;
    }

    public boolean getEstado() {
        return this.estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public User getCreadoPor() {
        return this.creadoPor;
    }

    public void setCreadoPor(User creadoPor) {
        this.creadoPor = creadoPor;
    }

    public List<User> getIntegrantes() {
        return this.integrantes;
    }

    public void setIntegrantes(List<User> integrantes) {
        this.integrantes = integrantes;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    
}
