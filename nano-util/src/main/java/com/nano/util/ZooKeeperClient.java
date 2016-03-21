package com.nano.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;

public class ZooKeeperClient {
	public final static String REGISTRY_ADDRESS = "master:2181,slave-01:2181,slave-02:2181";

	private ZooKeeperClient() {
	}

	private static class SingletonHolder {
		private static final ZooKeeperClient INSTANCE = new ZooKeeperClient();
	}

	public static final ZooKeeperClient getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private PathChildrenCache cache;

	private CuratorFramework client;

	public void init(String nameSpace) throws Exception {
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory
				.builder();
		client = builder
				.connectString(REGISTRY_ADDRESS)
				.sessionTimeoutMs(100000)
				.connectionTimeoutMs(100000)
				.canBeReadOnly(false)
				.retryPolicy(
						new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
				.namespace("nano").defaultData(null).build();
		client.start();
		cache = new PathChildrenCache(client, nameSpace, false);
		cache.start();
		PathChildrenCacheListener plis = new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				case CHILD_ADDED: {
					System.out
							.println("Node added: "
									+ ZKPaths.getNodeFromPath(event.getData()
											.getPath()));
					break;
				}
				case CHILD_UPDATED: {
					System.out
							.println("Node changed: "
									+ ZKPaths.getNodeFromPath(event.getData()
											.getPath()));
					break;
				}
				case CHILD_REMOVED: {
					System.out
							.println("Node removed: "
									+ ZKPaths.getNodeFromPath(event.getData()
											.getPath()));
					break;
				}
				default:
					break;
				}
			}
		};
		// 注册监听
		cache.getListenable().addListener(plis);
	}

	public CuratorFramework getCuratorFramework() throws Exception {
		return client;
	}
}
