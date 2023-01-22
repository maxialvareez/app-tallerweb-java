package com.tallerweb.apptallerwebjava.DAO;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.tallerweb.apptallerwebjava.models.Item;

public interface ItemRepository extends MongoRepository<Item, ObjectId> {
    
}
