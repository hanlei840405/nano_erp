package com.nano.application.system;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.nano.util.ZooKeeperClient;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.nano.service.system","com.nano.config.system"})
public class Application {

	public static void main(String[] args) throws Exception {
		ZooKeeperClient zooKeeperClient = ZooKeeperClient.getInstance();
		zooKeeperClient.init("/system/jdbc");
		CuratorFramework client = zooKeeperClient.getCuratorFramework();
		String driverClassName = new String(client.getData().watched().forPath("/system/jdbc/driverClassName"));
        String url = new String(client.getData().watched().forPath("/system/jdbc/url"));
        String username = new String(client.getData().watched().forPath("/system/jdbc/username"));
        String password = new String(client.getData().watched().forPath("/system/jdbc/password"));
        String initialSize = new String(client.getData().watched().forPath("/system/jdbc/initialSize"));
        String minIdle = new String(client.getData().watched().forPath("/system/jdbc/minIdle"));
        String maxActive = new String(client.getData().watched().forPath("/system/jdbc/maxActive"));
        String port = new String(client.getData().watched().forPath("/system/port"));
		System.setProperty("driverClassName", driverClassName);
		System.setProperty("url", url);
		System.setProperty("username", username);
		System.setProperty("password", password);
		System.setProperty("initialSize", initialSize);
		System.setProperty("minIdle", minIdle);
		System.setProperty("maxActive", maxActive);
		System.setProperty("server.port", port);
		SpringApplication.run(Application.class, args);
	}
}
