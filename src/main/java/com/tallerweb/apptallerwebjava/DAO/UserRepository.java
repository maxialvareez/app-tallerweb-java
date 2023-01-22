package com.tallerweb.apptallerwebjava.DAO;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tallerweb.apptallerwebjava.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    
    public Optional<User> findById(String id);
    public Optional<User> findByCorreo(String correo);

}
