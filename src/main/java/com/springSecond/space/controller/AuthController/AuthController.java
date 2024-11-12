package com.springSecond.space.controller.AuthController;
import com.springSecond.space.DTO.payloadDTO;
import com.springSecond.space.ResponseDTO.ResponseDTO;
import com.springSecond.space.models.UserEntity;
import com.springSecond.space.services.AuthServices.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v2/auth")
@Tag(name = "Authentication Controller", description = "Handles user authentication operations")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Operation(summary = "User Login", description = "Allows users to log in with their credentials")
    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO<Map<String, Object>>> login(@RequestBody payloadDTO.LoginDTO loginRequest) {
        logger.info("Received login request for username: {}", loginRequest.getUsername());

        try {
            ResponseDTO<Map<String, Object>> response = authService.login(loginRequest);

            if (response.getStatus().is2xxSuccessful()) {
                logger.info("Login successful for username: {}", loginRequest.getUsername());
            } else {
                logger.warn("Login failed for username: {}", loginRequest.getUsername());
            }

            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            logger.error("Error occurred during login for username: {}", loginRequest.getUsername(), e);

            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred during login",
                    "INTERNAL_SERVER_ERROR"
            );
            return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
        }
    }

    @Operation(summary = "User Register", description = "Allows users to Register account with their credentials")
    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO<Map<String, Object>>> createUser(@RequestBody UserEntity user) {
        logger.info("Received registration request for username: {}", user.getUsername());

        try {
            ResponseDTO<Map<String, Object>> response = authService.saveUser(user);

            if (response.getStatus().is2xxSuccessful()) {
                logger.info("User registered successfully: {}", user.getUsername());
            } else {
                logger.warn("Registration failed for username: {}", user.getUsername());
            }

            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            logger.error("Error occurred during registration for username: {}", user.getUsername(), e);

            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred during registration",
                    "INTERNAL_SERVER_ERROR"
            );
            return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
        }
    }
}
