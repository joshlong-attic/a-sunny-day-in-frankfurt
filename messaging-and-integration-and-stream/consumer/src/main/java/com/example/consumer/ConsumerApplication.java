package com.example.consumer;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;


@Log
@EnableBinding(Sink.class)
@SpringBootApplication
public class ConsumerApplication {


	@StreamListener(Sink.INPUT)
	public void incomingMessage(String msg) {
		log.info("new message! " + msg);
	}

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}