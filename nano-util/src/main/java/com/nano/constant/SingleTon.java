package com.nano.constant;

import com.nano.util.UUID;

public class SingleTon {
	private SingleTon() {
	}

	private static class SingletonHolder {
		private static final SingleTon INSTANCE = new SingleTon();
	}

	public static final SingleTon getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private SingleTonService singleTonService;

	public SingleTonService getSingleTonService() {
		return singleTonService;
	}

	public void setSingleTonService(SingleTonService singleTonService) {
		this.singleTonService = singleTonService;
	}

	public void print(String arg) {
		String uuid = UUID.generateShortUuid();
		singleTonService.first(uuid, arg);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		singleTonService.second(uuid, arg);
	}
}
