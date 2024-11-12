package com.springSecond.space.services.UdpClientServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

@Service
public class UdpClientService {

    private static final Logger logger = LoggerFactory.getLogger(UdpClientService.class);
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "localhost";

    public String sendMessage(String message) throws SocketException {
        logger.info("Attempting to send UDP message to server at {}:{}", SERVER_ADDRESS, SERVER_PORT);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000); // Set a timeout of 5 seconds

            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
            socket.send(sendPacket);
            logger.info("Message sent to UDP server: {}", message);

            byte[] receivedData = new byte[1024];
            DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);

            try {
                socket.receive(receivedPacket);
                String response = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                logger.info("Received response from UDP server: {}", response);
                return response;
            } catch (SocketTimeoutException e) {
                logger.warn("No response from UDP server (timeout occurred after 5 seconds).");
                return "Error: No response from server (timeout).";
            }

        } catch (Exception e) {
            logger.error("Error occurred while sending message to UDP server.", e);
            return "Error occurred while sending message.";
        }
    }
}
