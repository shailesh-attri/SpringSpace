package com.springSecond.space.controller.EmailsController;


import com.springSecond.space.DTO.payloadDTO;
import com.springSecond.space.ResponseDTO.ResponseDTO;
import com.springSecond.space.services.EmailServices.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v2/email")
public class EmailController {

    @Autowired
    private EmailService emailServices;

    // Logger for the controller
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    // Method to send an email based on the request body
    @Operation(summary = "Send email to specific user", description = "Allows to send emails")
    @PostMapping(value = "/send-mail", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO<Map<String, Object>>> sendOfferEmail(
            @RequestBody payloadDTO.EmailRequestDTO emailRequest) {
        try {
            // Logging the start of the process
            logger.info("Starting to send email to: {}", emailRequest.getToEmail());

            // Use the emailRequest object to get the details
            ResponseDTO<Map<String, Object>> response = emailServices.sendOfferEmail(emailRequest.getToEmail(), emailRequest.getOfferMessage());

            // Log the success of the operation
            logger.info("Email has been sent successfully to: {} with response: {}", emailRequest.getToEmail(), response);

            return new ResponseEntity<>(response, response.getStatus());
        } catch (Exception e) {
            // Log the error with the exception details
            logger.error("An error occurred while sending email to: {}. Error: {}", emailRequest.getToEmail(), e.getMessage(), e);

            // Prepare an error response
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred during email sending",
                    "INTERNAL_SERVER_ERROR"
            );
            return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
        }
    }

    // Scheduled task to send emails periodically
    @Scheduled(fixedRate = 600000)
    public void scheduledEmailTask() {
        // Predefined values for the scheduled task
        String toEmail = "shailesh.attri@axieva.com";
        String offerMessage = "Special Offer!";

        // Log the start of the scheduled email process
        logger.info("Scheduled task started to send email to: {}", toEmail);

        // Wrap the data into an EmailRequestDTO and call the service method
        payloadDTO.EmailRequestDTO emailRequest = new payloadDTO.EmailRequestDTO();
        emailRequest.setToEmail(toEmail);
        emailRequest.setOfferMessage(offerMessage);

        try {
            // Call the service to send the email
            emailServices.sendOfferEmail(emailRequest.getToEmail(), emailRequest.getOfferMessage());

            // Log the success of the scheduled email task
            logger.info("Scheduled email has been successfully sent to: {}", toEmail);
        } catch (Exception e) {
            // Log any exception that occurs during the scheduled task
            logger.error("Error occurred while sending scheduled email to: {}. Error: {}", toEmail, e.getMessage(), e);
        }
    }
}
