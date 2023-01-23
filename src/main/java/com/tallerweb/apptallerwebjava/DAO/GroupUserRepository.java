package com.tallerweb.apptallerwebjava.DAO;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tallerweb.apptallerwebjava.models.GroupUser;

@Repository
public interface GroupUserRepository extends MongoRepository<GroupUser, ObjectId> {

    @Query("{'integrantes.correo': ?0}")
    List<GroupUser> findByIntegrantesCorreo(String correo);
}
