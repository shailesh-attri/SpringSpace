package com.springSecond.space.services.UserDetailsService;


import com.springSecond.space.Repository.UserRepository.UserRepository;
import com.springSecond.space.models.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        ObjectId objectId;
        try {
            objectId = new ObjectId(userId);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Invalid user ID format: " + userId);
        }

        // Find the user by ObjectId
        UserEntity user = userRepository.findById(objectId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        // If roles is null, provide an empty list instead
        List<String> roles = user.getRoles() != null ? user.getRoles() : Collections.emptyList();

        // Build UserDetails object using the UserEntity
        return User.builder()
                .username(user.getId().toHexString())
                .password(user.getPassword())
                .roles(roles.toArray(new String[0])) // Convert roles to a string array
                .build();
    }
}
