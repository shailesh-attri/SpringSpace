package com.springSecond.space.services.UdpServerServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;

@Service
public class UdpServerServices {

    private static final Logger logger = LoggerFactory.getLogger(UdpServerServices.class);

    private static final int PORT = 8080;
    private static final String FAILURE_RESPONSE = "Error: Failed to process your message. Please try again later.";

    public void startUdpServer() {
        Thread udpThread = new Thread(() -> {
            logger.info("Initializing UDP server on port {}", PORT);
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket(PORT);
                logger.info("UDP server started successfully on port {} and is now listening for messages...", PORT);

                byte[] receivedData = new byte[1024];

                while (true) {
                    DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
                    socket.receive(receivedPacket); // This line blocks until a message is received
                    String receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                    logger.info("Received message: {}", receivedMessage);

                    // Create a response message with the received message included
                    String responseMessage = "Thank you! Your message has been received and processed by our UDP Server.\n" +
                            "Here is your message: " + receivedMessage;

                    // Respond with the dynamic response
                    sendResponse(socket, receivedPacket.getAddress(), receivedPacket.getPort(), responseMessage);
                    logger.info("Successfully processed and responded to the message.");
                }

            } catch (SocketException e) {
                logger.error("Failed to start UDP server on port {}", PORT, e);
            } catch (IOException e) {
                logger.error("Error receiving message in the UDP server", e);
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                logger.info("UDP server has stopped.");
            }
        });

        udpThread.setDaemon(false); // Ensure the thread keeps running independently
        udpThread.start(); // Start the thread to run the UDP server
        logger.info("UDP server thread has been started.");
    }

    private void sendResponse(DatagramSocket socket, InetAddress clientAddress, int clientPort, String responseMessage) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(responseMessage.getBytes(), responseMessage.length(), clientAddress, clientPort);
            socket.send(sendPacket);  // Send the response back to the client
            logger.info("Response sent to client at {}:{} - {}", clientAddress, clientPort, responseMessage);
        } catch (IOException e) {
            logger.error("Failed to send response: {}", responseMessage, e);
        }
    }
}
