package com.nano.config.productmapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.nano.service.productmapping.DangDangProductService;
import com.nano.service.productmapping.JingDongProductService;
import com.nano.service.productmapping.TaoBaoProductService;

@Configuration
@EnableAutoConfiguration
@ImportResource("classpath:dubbo.xml")
public class DubboConfig {

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private RegistryConfig registryConfig;

	@Autowired
	private ProtocolConfig protocolConfig;

	@Autowired
	private TaoBaoProductService taoBaoProductService;

	@Autowired
	private JingDongProductService jingDongProductService;

	@Autowired
	private DangDangProductService dangDangProductService;

	@Bean
	public ServiceConfig<TaoBaoProductService> initTaoBaoProductServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<TaoBaoProductService> service = new ServiceConfig<TaoBaoProductService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(TaoBaoProductService.class);
		service.setRef(taoBaoProductService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<JingDongProductService> initJingDongProductServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<JingDongProductService> service = new ServiceConfig<JingDongProductService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(JingDongProductService.class);
		service.setRef(jingDongProductService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<DangDangProductService> initDangDangProductServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<DangDangProductService> service = new ServiceConfig<DangDangProductService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(DangDangProductService.class);
		service.setRef(dangDangProductService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}
}
