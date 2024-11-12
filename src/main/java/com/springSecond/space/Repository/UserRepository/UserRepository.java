package com.springSecond.space.Repository.UserRepository;

import com.springSecond.space.models.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
    UserEntity findByEmail(String email);  // Define this method
    UserEntity findByUsername(String username);
}
