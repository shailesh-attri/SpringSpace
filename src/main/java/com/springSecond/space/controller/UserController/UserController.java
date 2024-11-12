package com.springSecond.space.controller.UserController;

import com.springSecond.space.DTO.payloadDTO;
import com.springSecond.space.ResponseDTO.ResponseDTO;
import com.springSecond.space.services.UserServices.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api/v2/user")
public class UserController {

    @Autowired
    private UserServices userServices;

    // Logger for the controller
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Method to fetch all users with scheduled task
    @Operation(summary = "Get all users", description = "Allows to get all users")
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Scheduled(fixedRate = 15000)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getUser() {
        try {
            // Log the start of fetching user list
            logger.info("Fetching user list...");
            ResponseDTO<Map<String, Object>> response = userServices.getUser();

            // Log the successful response
            logger.info("Fetched user list: {}", response);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            // Log the error with exception details
            logger.error("An error occurred while fetching user list: {}", e.getMessage(), e);

            // Custom error response
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred during fetching users",
                    "INTERNAL_SERVER_ERROR"
            );
            return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
        }
    }

    // Method to get a user by id
    @Operation(summary = "Get user by id", description = "Allows to get a user")
    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getUserById(@PathVariable String userId) {
        try {
            // Log the attempt to fetch a user by ID
            logger.info("Fetching user with ID: {}", userId);
            ResponseDTO<Map<String, Object>> response = userServices.getUserById(userId);

            // Log the successful response
            logger.info("Fetched user with ID: {}: {}", userId, response);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            // Log the error with exception details
            logger.error("An error occurred while fetching user with ID: {}. Error: {}", userId, e.getMessage(), e);

            // Custom error response
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while fetching user",
                    "INTERNAL_SERVER_ERROR"
            );
            return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
        }
    }

    // Method to update a user by ID
    @Operation(summary = "Update user by id", description = "Allows to update a user")
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO<Map<String, Object>>> updateUser(@RequestBody payloadDTO.RegisterDTO request) {
        try {
            // Log the update request
            logger.info("Updating user with details: {}");
            ResponseDTO<Map<String, Object>> response = userServices.updateUserById(request);

            // Log the successful response
            logger.info("User updated successfully: {}", response);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            // Log the error with exception details
            logger.error("An error occurred while updating user. Error: {}", e.getMessage(), e);

            // Custom error response
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred during user update",
                    "INTERNAL_SERVER_ERROR"
            );
            return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
        }
    }

    // Method to upload an image
    @Operation(summary = "Upload image", description = "Allows to upload image for user")
    @PostMapping("/upload-image")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> uploadImage(@PathParam("file") MultipartFile file) {
        try {
            // Log the attempt to upload an image
            logger.info("Uploading image with file name: {}", file.getOriginalFilename());
            ResponseDTO<Map<String, Object>> response = userServices.uploadImage(file);

            // Log the successful image upload
            logger.info("Image uploaded successfully: {}", response);
            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            // Log the error with exception details
            logger.error("An error occurred during image upload. Error: {}", e.getMessage(), e);

            // Custom error response
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred during image upload",
                    "INTERNAL_SERVER_ERROR"
            );
            return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
        }
    }
}
