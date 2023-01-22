package com.tallerweb.apptallerwebjava.DAO;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tallerweb.apptallerwebjava.models.GroupUser;

@Repository
public interface GroupUserRepository extends MongoRepository<GroupUser, ObjectId> {
    
}
