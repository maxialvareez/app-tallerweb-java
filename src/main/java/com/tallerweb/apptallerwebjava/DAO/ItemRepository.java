package com.tallerweb.apptallerwebjava.DAO;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tallerweb.apptallerwebjava.models.Item;

public interface ItemRepository extends MongoRepository<Item, ObjectId> {

    public Optional<Item> findById(String id);
    
}
