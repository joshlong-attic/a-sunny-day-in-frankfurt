package com.example.producer;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;


interface ProducerChannels {

	@Output
	MessageChannel strings();
}

@Log
@EnableBinding(ProducerChannels.class)
@SpringBootApplication
public class ProducerApplication {

	@Bean
	IntegrationFlow files(@Value("file://${HOME}/Desktop/in") File newFiles,
	                      ProducerChannels producerChannels) {

		return
				IntegrationFlows
						.from(Files.inboundAdapter(newFiles)
								.autoCreateDirectory(true), pollerSpec -> pollerSpec.poller(pm -> pm.fixedRate(1000)))
						.transform(new FileToStringTransformer())
						.channel(producerChannels.strings())
						.get();
	}

/*
	@Bean
	IntegrationFlow stringsFlow(AmqpTemplate amqpTemplate) {
		return IntegrationFlows
				.from(lowercaseStrings())
				.handle(Amqp.outboundAdapter(amqpTemplate).exchangeName("greetings").routingKey("greetings"))
				.get();
	}
*/

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}
}
/*

@Configuration
class RabbitConfiguration {

	private String greetings = "greetings";

	@Bean
	Queue greetings() {
		return QueueBuilder.durable(greetings).build();
	}

	@Bean
	Exchange exchange() {
		return ExchangeBuilder.directExchange(greetings).durable(true).build();
	}

	@Bean
	Binding binding() {
		return BindingBuilder.bind(greetings()).to(exchange()).with(greetings).noargs();
	}

}
*/


@RestController
class InboundStringsRestController {

	private final MessageChannel strings;

	InboundStringsRestController(ProducerChannels pc) {
		this.strings = pc.strings();
	}

	@GetMapping("/hi/{name}")
	void post(@PathVariable String name) {
		Message<String> build = MessageBuilder.withPayload(name)
				.build();
		this.strings.send(build);
	}
}