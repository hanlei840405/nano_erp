package com.nano.config.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.nano.service.base.BrandCategoryService;
import com.nano.service.base.BrandService;
import com.nano.service.base.CategoryService;
import com.nano.service.base.ContactService;
import com.nano.service.base.DictionaryItemService;
import com.nano.service.base.DictionaryService;
import com.nano.service.base.ExpressService;
import com.nano.service.base.MarkService;
import com.nano.service.base.MemberService;
import com.nano.service.base.NoteService;
import com.nano.service.base.PlatformExpressService;
import com.nano.service.base.PlatformService;
import com.nano.service.base.ProductService;
import com.nano.service.base.RegionExpressService;
import com.nano.service.base.RegionService;
import com.nano.service.base.ShopCategoryService;
import com.nano.service.base.ShopPropertyService;
import com.nano.service.base.ShopService;
import com.nano.service.base.SkuService;
import com.nano.service.base.SmsService;
import com.nano.service.base.SmsTemplateService;
import com.nano.service.base.StockService;
import com.nano.service.base.StorageService;
import com.nano.service.base.WarehouseBrandService;
import com.nano.service.base.WarehouseService;
import com.nano.util.ZooKeeperClient;

@Configuration
@EnableAutoConfiguration
public class DubboConfig {
	public static final String APPLICATION_NAME = "base";

	private ApplicationConfig applicationConfig;

	private RegistryConfig registryConfig;

	private ProtocolConfig protocolConfig;

	@Autowired
	private BrandCategoryService brandCategoryService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ExpressService expressService;
	@Autowired
	private MarkService markService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NoteService noteService;
	@Autowired
	private PlatformExpressService platformExpressService;
	@Autowired
	private PlatformService platformService;
	@Autowired
	private ProductService productService;
	@Autowired
	private RegionExpressService regionExpressService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopPropertyService shopPropertyService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private SmsTemplateService smsTemplateService;
	@Autowired
	private StockService stockService;
	@Autowired
	private WarehouseBrandService warehouseBrandService;
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private DictionaryItemService dictionaryItemService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private StorageService storageService;

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
		// config.setSerialization("kryo");
		config.setPort(20881);
		protocolConfig = config;
		return config;
	}

	@Bean
	public ServiceConfig<BrandCategoryService> initBrandCategoryServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<BrandCategoryService> service = new ServiceConfig<BrandCategoryService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(BrandCategoryService.class);
		service.setRef(brandCategoryService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<BrandService> initBrandServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<BrandService> service = new ServiceConfig<BrandService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(BrandService.class);
		service.setRef(brandService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<CategoryService> initCategoryServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<CategoryService> service = new ServiceConfig<CategoryService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(CategoryService.class);
		service.setRef(categoryService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<ExpressService> initExpressServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<ExpressService> service = new ServiceConfig<ExpressService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(ExpressService.class);
		service.setRef(expressService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<MarkService> initMarkServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<MarkService> service = new ServiceConfig<MarkService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(MarkService.class);
		service.setRef(markService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<MemberService> initMemberServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<MemberService> service = new ServiceConfig<MemberService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(MemberService.class);
		service.setRef(memberService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<NoteService> initNoteServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<NoteService> service = new ServiceConfig<NoteService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(NoteService.class);
		service.setRef(noteService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<PlatformExpressService> initPlatformExpressServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<PlatformExpressService> service = new ServiceConfig<PlatformExpressService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(PlatformExpressService.class);
		service.setRef(platformExpressService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<PlatformService> initPlatformServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<PlatformService> service = new ServiceConfig<PlatformService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(PlatformService.class);
		service.setRef(platformService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<ProductService> initProductServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<ProductService> service = new ServiceConfig<ProductService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(ProductService.class);
		service.setRef(productService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<RegionExpressService> initRegionExpressServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<RegionExpressService> service = new ServiceConfig<RegionExpressService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(RegionExpressService.class);
		service.setRef(regionExpressService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<RegionService> initRegionServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<RegionService> service = new ServiceConfig<RegionService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(RegionService.class);
		service.setRef(regionService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<ShopCategoryService> initShopCategoryServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<ShopCategoryService> service = new ServiceConfig<ShopCategoryService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(ShopCategoryService.class);
		service.setRef(shopCategoryService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<ShopPropertyService> initShopPropertyServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<ShopPropertyService> service = new ServiceConfig<ShopPropertyService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(ShopPropertyService.class);
		service.setRef(shopPropertyService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<ShopService> initShopServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<ShopService> service = new ServiceConfig<ShopService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(ShopService.class);
		service.setRef(shopService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<SkuService> initSkuServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<SkuService> service = new ServiceConfig<SkuService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(SkuService.class);
		service.setRef(skuService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<SmsService> initSmsServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<SmsService> service = new ServiceConfig<SmsService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(SmsService.class);
		service.setRef(smsService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<SmsTemplateService> initSmsTemplateServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<SmsTemplateService> service = new ServiceConfig<SmsTemplateService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(SmsTemplateService.class);
		service.setRef(smsTemplateService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<StockService> initStockServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<StockService> service = new ServiceConfig<StockService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(StockService.class);
		service.setRef(stockService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<WarehouseBrandService> initWarehouseBrandServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<WarehouseBrandService> service = new ServiceConfig<WarehouseBrandService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(WarehouseBrandService.class);
		service.setRef(warehouseBrandService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<WarehouseService> initWarehouseServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<WarehouseService> service = new ServiceConfig<WarehouseService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(WarehouseService.class);
		service.setRef(warehouseService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<DictionaryService> initDictionaryServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<DictionaryService> service = new ServiceConfig<DictionaryService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(DictionaryService.class);
		service.setRef(dictionaryService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<DictionaryItemService> initDictionaryItemServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<DictionaryItemService> service = new ServiceConfig<DictionaryItemService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(DictionaryItemService.class);
		service.setRef(dictionaryItemService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<ContactService> initContactServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<ContactService> service = new ServiceConfig<ContactService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(ContactService.class);
		service.setRef(contactService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}

	@Bean
	public ServiceConfig<StorageService> initStorageServiceConfig() {
		// 服务提供者暴露服务配置
		ServiceConfig<StorageService> service = new ServiceConfig<StorageService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(applicationConfig);
		service.setRegistry(registryConfig); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocolConfig); // 多个协议可以用setProtocols()
		service.setInterface(StorageService.class);
		service.setRef(storageService);
		service.setVersion("1.0.0");
		// 暴露及注册服务
		service.export();
		return service;
	}
}
