package com.springSecond.space.controller.UdpController;


import com.springSecond.space.services.UdpClientServices.UdpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;

@RestController
@RequestMapping("api/v2")
public class UdpTestController {
    private static final Logger logger = LoggerFactory.getLogger(UdpTestController.class);
    @Autowired
    private UdpClientService udpClientService;

    @PostMapping("/send-message")
    public String testUdp(@RequestBody String message) {

        try {
            return udpClientService.sendMessage(message); // Call to the service
        } catch (SocketException e) {
            return "Error: SocketException occurred in the client.";
        } catch (Exception e) {
            return "Error: Something went wrong while communicating with the server.";
        }
    }
}