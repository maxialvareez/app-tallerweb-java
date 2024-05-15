package com.tallerweb.apptallerwebjava.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Document("users")
public class User {
    
    @Id
    private ObjectId id;
    private String nombre;
    private String correo;
    private String password;
    private boolean estado;

    public User(String nombre, String correo, String password){
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.estado = true;
    }

    public User(){
        this.estado = true;
    }

    public boolean getEstado() {
        return this.estado;
    }
}
