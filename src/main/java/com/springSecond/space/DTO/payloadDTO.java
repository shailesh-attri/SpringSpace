package com.springSecond.space.DTO;

import lombok.Getter;
import lombok.Setter;

public class payloadDTO {

    @Getter
    @Setter
    public static class LoginDTO {  // Make this class static
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Getter
    @Setter
    public static class RegisterDTO {  // Make this class static
        private String email;
        private String username;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Getter
    @Setter
    public static class AuthResponseDTO {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @Getter
    @Setter
    public static class EmailRequestDTO {
        private String toEmail;
        private String offerMessage;

        public String getToEmail() {
            return toEmail;
        }

        public void setToEmail(String toEmail) {
            this.toEmail = toEmail;
        }

        public String getOfferMessage() {
            return offerMessage;
        }

        public void setOfferMessage(String offerMessage) {
            this.offerMessage = offerMessage;
        }
    }
}
