package com.springSecond.space.services.UserServices;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.springSecond.space.DTO.payloadDTO;
import com.springSecond.space.Repository.UserRepository.UserRepository;
import com.springSecond.space.ResponseDTO.ResponseDTO;
import com.springSecond.space.ResponseDTO.UserObject;
import com.springSecond.space.models.UserEntity;
import com.springSecond.space.utils.SecurityUtil;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserObject userObject;
    private static final Logger logger = LoggerFactory.getLogger(UserServices.class);

    public ResponseDTO<Map<String, Object>> getUser() {
        List<UserEntity> users = userRepository.findAll();
        List<Map<String, Object>> userList = new ArrayList<>();
        for (UserEntity user : users) {
            Map<String, Object> response = userObject.mappingUser(user);
            userList.add(response);
        }
        Map<String, Object> responseDetails = new HashMap<>();
        responseDetails.put("user", userList);

        return ResponseDTO.success(responseDetails, "User list fetched successfully");
    }

    public ResponseDTO<Map<String, Object>> getUserById(String userId) {
        try {
            ObjectId objectId = new ObjectId(userId); // Direct conversion of string to ObjectId
            Optional<UserEntity> userOptional = userRepository.findById(objectId);

            if (userOptional.isEmpty()) {
                return ResponseDTO.error(HttpStatus.NOT_FOUND, "User not found", "USER_NOT_FOUND");
            }

            UserEntity user = userOptional.get();
            Map<String, Object> response = userObject.mappingUser(user);

            return ResponseDTO.success(response, "User found successfully");

        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Invalid user ID format: " + userId);
        }
    }

    public ResponseDTO<Map<String, Object>> updateUserById(payloadDTO.RegisterDTO request) {
        String userId = SecurityUtil.getUserIdFromSecurityContext();
        assert userId != null;

        if (userId.trim().isEmpty()) return ResponseDTO.error(HttpStatus.BAD_REQUEST, "Invalid user ID", "USER_ID_INVALID");
        ObjectId objectId = new ObjectId(userId);

        Optional<UserEntity> userOptional = userRepository.findById(objectId);

        if(userOptional.isEmpty()) return ResponseDTO.error(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found");
        // Check if userId is not null or empty
        String email=request.getEmail();
        String username=request.getUsername();
        UserEntity user = userOptional.get();

        user.setUsername(username != null ? username : user.getUsername());
        user.setEmail(email != null ? email : user.getEmail());

        UserEntity UpdatedUser = userRepository.save(user);

        Map<String, Object> response = userObject.mappingUser(UpdatedUser);

        return ResponseDTO.success(response, "User updated successfully");
    }

    public ResponseDTO<Map<String, Object>> uploadImage(MultipartFile file) {
        String userId = SecurityUtil.getUserIdFromSecurityContext();
        assert userId != null;

        Optional<UserEntity> optionalUser = (Optional<UserEntity>) userRepository.findById(new ObjectId(userId));
        if (optionalUser.isEmpty())
            return ResponseDTO.error(HttpStatus.NOT_FOUND, "User not found", "USER_NOT_FOUND");

        UserEntity user = optionalUser.get();
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String publicId = (String) uploadResult.get("public_id");
            String secureUrl = (String) uploadResult.get("secure_url");

            user.setImageUrl(secureUrl != null ? secureUrl : user.getImageUrl());
            user.setImagePublicId(publicId != null ? publicId : user.getImagePublicId());

            UserEntity updatedUser = userRepository.save(user);
            Map<String, Object> response = userObject.mappingUser(updatedUser);

            return ResponseDTO.success(response, "Image uploaded successfully");

        } catch (Exception e) {
            logger.error("Failed to upload image", e);
            throw new RuntimeException("Failed to upload image", e);
        }
    }

}