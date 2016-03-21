package com.nano.application.base;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.nano.util.ZooKeeperClient;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.nano.service.base","com.nano.config.base"})
public class Application {

	public static void main(String[] args) throws Exception {
		ZooKeeperClient zooKeeperClient = ZooKeeperClient.getInstance();
		zooKeeperClient.init("/base/jdbc");
		CuratorFramework client = zooKeeperClient.getCuratorFramework();
		String driverClassName = new String(client.getData().watched().forPath("/base/jdbc/driverClassName"));
        String url = new String(client.getData().watched().forPath("/base/jdbc/url"));
        String username = new String(client.getData().watched().forPath("/base/jdbc/username"));
        String password = new String(client.getData().watched().forPath("/base/jdbc/password"));
        String initialSize = new String(client.getData().watched().forPath("/base/jdbc/initialSize"));
        String minIdle = new String(client.getData().watched().forPath("/base/jdbc/minIdle"));
        String maxActive = new String(client.getData().watched().forPath("/base/jdbc/maxActive"));
        String port = new String(client.getData().watched().forPath("/base/port"));
		System.setProperty("driverClassName", driverClassName);
		System.setProperty("url", url);
		System.setProperty("username", username);
		System.setProperty("password", password);
		System.setProperty("initialSize", initialSize);
		System.setProperty("minIdle", minIdle);
		System.setProperty("maxActive", maxActive);
		System.setProperty("server.port", port);

        String mqHost = new String(client.getData().watched().forPath("/rabbitmq/host"));
        String mqPort = new String(client.getData().watched().forPath("/rabbitmq/port"));
        String mqUsername = new String(client.getData().watched().forPath("/rabbitmq/username"));
        String mqPassword = new String(client.getData().watched().forPath("/rabbitmq/password"));
        System.setProperty("mqHost", mqHost);
		System.setProperty("mqPort", mqPort);
		System.setProperty("mqUsername", mqUsername);
		System.setProperty("mqPassword", mqPassword);
        SpringApplication.run(Application.class, args);
	}
}
