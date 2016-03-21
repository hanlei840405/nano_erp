package com.nano.config.productmapping;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

	@Value("${mqHost}")
	private String host;

	@Value("${mqPort}")
	private int port;

	@Value("${mqUsername}")
	private String username;

	@Value("${mqPassword}")
	private String password;

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		CachingConnectionFactory cf = new CachingConnectionFactory(host, port);
		cf.setUsername(username);
		cf.setPassword(password);
		return cf;
	}

	@Bean
	public Queue transactionQueue() {
		Queue queue = new Queue("com.nano.transaction.queue", true);
		return queue;
	}

	@Bean
	
	public DirectExchange transactionDirectExchange() {
		DirectExchange directExchange = new DirectExchange(
				"com.nano.transaction.exchange");
		return directExchange;
	}

	@Bean
	public Binding transactionBinding() {
		return BindingBuilder.bind(transactionQueue()).to(
				transactionDirectExchange()).with("transaction");
	}
}
