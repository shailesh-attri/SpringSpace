package com.springSecond.space.services.EmailServices;
import com.springSecond.space.ResponseDTO.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServices {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailServices.class);

    private static final String SENDER_EMAIL = "shaileshattri83@gmail.com"; // Make sure to externalize this

    public ResponseDTO<Map<String, Object>> sendOfferEmail(String toEmail, String offerMessage) {
        SimpleMailMessage message = createEmailMessage(toEmail, offerMessage);

        try {
            javaMailSender.send(message);
            Map<String, Object> responseDetails = new HashMap<>();
            responseDetails.put("status", "success");
            responseDetails.put("to", toEmail);

            logger.info("Email sent successfully to {}", toEmail);

            return ResponseDTO.success(responseDetails, "Offer email sent successfully.");
        } catch (Exception e) {
            logger.error("Failed to send mail to {}", toEmail, e);

            return ResponseDTO.error(HttpStatus.BAD_REQUEST, "Failed to send email.", "FAILED");
        }
    }

    // Helper method to create the email message
    private SimpleMailMessage createEmailMessage(String toEmail, String offerMessage) {
        String subject = "Amazing Offer Just for You!";
        String messageBody = String.format("Hello there!\n\n%s\n\nUse our new Spring Boot backend services for all your needs! " +
                "Don't miss out on this exclusive offer. Contact us for more details.\n\nBest regards,\nAxieva, Noida", offerMessage);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(messageBody);
        message.setFrom(SENDER_EMAIL); // Ensure this is configurable
        return message;
    }
}
