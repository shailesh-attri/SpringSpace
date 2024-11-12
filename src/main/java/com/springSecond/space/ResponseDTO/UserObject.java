package com.springSecond.space.ResponseDTO;

import org.springframework.stereotype.Component;
import com.springSecond.space.models.UserEntity;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserObject {

    /**
     * Maps UserEntity to a response map with the necessary user details.
     * This method directly uses the data inside the UserEntity, without the need for the 'isNewUser' check.
     *
     * @param user the user entity to map
     * @return a map containing user details
     */
    public Map<String, Object> mappingUser(UserEntity user) {
        Map<String, Object> response = new HashMap<>();

        response.put("id", user.getId().toHexString());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());

        // Directly add imageUrl and imagePublicId from the user entity, or provide a default value if not available.
        response.put("imageUrl", user.getImageUrl() != null ? user.getImageUrl() : "Default Image URL");
        response.put("imagePublicId", user.getImagePublicId() != null ? user.getImagePublicId() : "Default Public ID");

        return response;
    }
}
