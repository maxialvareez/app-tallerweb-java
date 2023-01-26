package com.tallerweb.apptallerwebjava.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties.Decryption;
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
@Document("groupusers")
public class GroupUser {

    @Id
    private ObjectId id;
    private String nombre;
    private String descripcion;
    private boolean estado;
    private User creadoPor;
    private List<User> integrantes;
    private List<Item> items;


    public GroupUser(String nombre, String descripcion, User creadoPor){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = true;
        this.creadoPor = creadoPor;
        this.integrantes = new ArrayList<>();
        this.integrantes.add(creadoPor);
        this.items = new ArrayList<>();
    }

    public void addIntegrante(User user){
        this.integrantes.add(user);        
    }

    public void deleteIntegrante(User user){
        if(this.integrantes.contains(user)){
            this.integrantes.remove(user);
        }       
    }
    
}
