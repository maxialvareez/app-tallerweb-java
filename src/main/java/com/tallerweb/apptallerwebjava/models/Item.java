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
@AllArgsConstructor
@Document("items")
public class Item {
    
    @Id
    private ObjectId id;
    private String nombre;
    private String descripcion;
    private Double costo;
    private Boolean pago;
    private boolean estado;
    private User creadoPor;

    public Item(String nombre, String descripcion, Double costo, Boolean pago, boolean estado, User creadoPor) {
        this.id = new ObjectId();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.pago = pago;
        this.estado = estado;
        this.creadoPor = creadoPor;
    }

    public Item(){
        this.estado = true;
        this.pago = false;
    }

}
