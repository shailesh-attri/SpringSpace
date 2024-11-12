package com.springSecond.space.services.AuthServices;

import com.springSecond.space.DTO.payloadDTO;
import com.springSecond.space.Repository.UserRepository.UserRepository;
import com.springSecond.space.ResponseDTO.ResponseDTO;
import com.springSecond.space.models.UserEntity;
import com.springSecond.space.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Login method, accepts either email or username and matches password.
     */
    public ResponseDTO<Map<String, Object>> login(payloadDTO.LoginDTO request) {
        String identifier = request.getUsername(); // This will be either username or email
        String password = request.getPassword();
        UserEntity user = null;

        try {
            // Check if identifier contains @ symbol (email)
            if (isEmail(identifier)) {
                // Find user by email
                user = userRepository.findByEmail(identifier);
            } else {
                // Find user by username
                user = userRepository.findByUsername(identifier);
            }

            if (user == null) {
                return ResponseDTO.error(HttpStatus.NOT_FOUND, "User not found", "USER_NOT_FOUND");
            }

            // Check if password matches the encoded password stored in the database
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseDTO.error(HttpStatus.UNAUTHORIZED, "Invalid credentials", "INVALID_CREDENTIALS");
            }

            // Generate JWT token
            String token = jwtUtils.generateToken(user.getId().toHexString());

            // Update last login time
            user.setLastLogin(String.valueOf(new Date()));
            userRepository.save(user);

            // Prepare response
            Map<String, Object> responseDetails = new HashMap<>();
            responseDetails.put("email", user.getEmail());
            responseDetails.put("userId", user.getId().toHexString());
            responseDetails.put("authToken", token);
            responseDetails.put("success", true);
            responseDetails.put("lastLogin", user.getLastLogin());
            responseDetails.put("imageUrl", user.getImageUrl() != null ? user.getImageUrl() : null);
            responseDetails.put("imagePublicId", user.getImagePublicId() != null ? user.getImagePublicId() : null);

            return ResponseDTO.success(responseDetails, "Login successful");

        } catch (DataAccessException e) {
            logger.error("Database error during login: ", e);
            return ResponseDTO.error(HttpStatus.INTERNAL_SERVER_ERROR, "Database error during login", "DATABASE_ERROR");
        } catch (Exception e) {
            logger.error("Unexpected error during login: ", e);
            return ResponseDTO.error(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during login", "INTERNAL_SERVER_ERROR");
        }
    }

    /**
     * Method to register a new user.
     * Ensures that the username and email are unique.
     */
    public ResponseDTO<Map<String, Object>> saveUser(UserEntity user) {
        try {
            // Check for duplicate email or username
            if (userRepository.findByEmail(user.getEmail()) != null) {
                return ResponseDTO.error(HttpStatus.BAD_REQUEST, "Email already in use", "EMAIL_DUPLICATE");
            }
            if (userRepository.findByUsername(user.getUsername()) != null) {
                return ResponseDTO.error(HttpStatus.BAD_REQUEST, "Username already in use", "USERNAME_DUPLICATE");
            }

            // Encode the password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            UserEntity savedUser = userRepository.save(user);

            // Generate JWT token for the newly created user
            String token = jwtUtils.generateToken(savedUser.getId().toHexString());

            Map<String, Object> responseDetails = new HashMap<>();
            responseDetails.put("id", savedUser.getId().toHexString());
            responseDetails.put("username", savedUser.getUsername());
            responseDetails.put("email", savedUser.getEmail());
            responseDetails.put("authToken", token);
            responseDetails.put("imageUrl", user.getImageUrl() != null ? user.getImageUrl() : null);
            responseDetails.put("imagePublicId", user.getImagePublicId() != null ? user.getImagePublicId() : null);

            return ResponseDTO.success(responseDetails, "User created successfully");

        } catch (DataAccessException e) {
            logger.error("Database error during user registration: ", e);
            return ResponseDTO.error(HttpStatus.INTERNAL_SERVER_ERROR, "Database error during registration", "DATABASE_ERROR");
        } catch (Exception e) {
            logger.error("Unexpected error during user registration: ", e);
            return ResponseDTO.error(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during registration", "INTERNAL_SERVER_ERROR");
        }
    }

    /**
     * Helper method to check if the identifier is an email.
     */
    private boolean isEmail(String identifier) {
        // Regular expression for validating an email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(identifier).matches();
    }
}
