package com.tallerweb.apptallerwebjava.models;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("users")
public class User {
    
    @Id
    private ObjectId id;
    private String nombre;
    private String correo;
    private String password;
    private boolean estado;
    private GroupUser pertenece_a;

    public User(String nombre, String correo, String password){
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.estado = true;
    }


}
