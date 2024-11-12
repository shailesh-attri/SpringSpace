package com.springSecond.space;


import com.springSecond.space.services.UdpServerServices.UdpServerServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@EnableScheduling
@SpringBootApplication
@EnableAsync
public class SecondApplication {

	private static final Logger logger = LoggerFactory.getLogger(SecondApplication.class);

	@Autowired
	private Environment environment;

	@Autowired
	private UdpServerServices udpServices;

	public static void main(String[] args) {
		SpringApplication.run(SecondApplication.class, args);
//		logger.info("MyFirstProjectApplication is starting...");
	}

	@PostConstruct
	public void logApplicationStartup() {
		String port = environment.getProperty("server.port", "8080");  // Default to 8080 if not set
		logger.info("Application has  started successfully and running on port: {}", port);

		logger.info("Starting UDP server...");
		udpServices.startUdpServer();
		logger.info("UDP server initialization triggered.");
	}
}