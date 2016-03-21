package com.nano.config.system;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.nano.service.system.DepartmentService;
import com.nano.service.system.ElementService;
import com.nano.service.system.FunctionService;
import com.nano.service.system.GroupService;
import com.nano.service.system.MenuService;
import com.nano.service.system.PrivilegeService;
import com.nano.service.system.RoleService;
import com.nano.service.system.UserService;
import com.nano.util.ZooKeeperClient;

@Configuration
@EnableAutoConfiguration
public class DubboConfig {
	private static final Logger LOG = Logger.getLogger(DubboConfig.class);

	public static final String APPLICATION_NAME = "system";

	private ApplicationConfig applicationConfig;

	private RegistryConfig registryConfig;

	private ProtocolConfig protocolConfig;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private ElementService elementService;

	@Autowired
	private FunctionService functionService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private PrivilegeService privilegeService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Bean
	public ApplicationConfig initApplicationConfig() {
		ApplicationConfig config = new ApplicationConfig();
		config.setName(APPLICATION_NAME);
		applicationConfig = config;
		return config;
	}

	@Bean
	public RegistryConfig initRegistryConfig() {
		RegistryConfig config = new RegistryConfig();
		config.setAddress(ZooKeeperClient.REGISTRY_ADDRESS);
		config.setProtocol("zookeeper");
		registryConfig = config;
		return config;
	}

	@Bean
	public ProtocolConfig initProtocolConfig() {
		ProtocolConfig config = new ProtocolConfig();
		config.setName("dubbo");
		config.setPort(20880);
//		config.setSerialization("kryo");
		protocolConfig = config;
		return config;
	}

	@Bean
	public ServiceConfig<DepartmentService> initDepartmentServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<DepartmentService> service = new ServiceConfig<DepartmentService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(DepartmentService.class);
		service.setRef(departmentService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<UserService> initUserServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<UserService> service = new ServiceConfig<UserService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(UserService.class);
		service.setRef(userService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<ElementService> initElementServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<ElementService> service = new ServiceConfig<ElementService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(ElementService.class);
		service.setRef(elementService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<FunctionService> initFunctionServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<FunctionService> service = new ServiceConfig<FunctionService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(FunctionService.class);
		service.setRef(functionService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<GroupService> initGroupServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<GroupService> service = new ServiceConfig<GroupService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(GroupService.class);
		service.setRef(groupService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<MenuService> initMenuServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<MenuService> service = new ServiceConfig<MenuService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(MenuService.class);
		service.setRef(menuService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<PrivilegeService> initPrivilegeServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<PrivilegeService> service = new ServiceConfig<PrivilegeService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(PrivilegeService.class);
		service.setRef(privilegeService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<RoleService> initRoleServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<RoleService> service = new ServiceConfig<RoleService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(RoleService.class);
		service.setRef(roleService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}
}
