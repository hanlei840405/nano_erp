package com.nano.constant;


public class SingleTonService {
	private SingleTonService() {
	}

	private static class SingletonHolder {
		private static final SingleTonService INSTANCE = new SingleTonService();
	}

	public static final SingleTonService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void first(String arg0,String arg1) {
		System.out.println(String.format("【%s】【%s】打印开始...", arg0,arg1));
		System.out.println(String.format("【%s】首次打印内容为:【%s】", arg0, arg1));
	}

	public void second(String arg0,String arg1) {
		System.out.println(String.format("【%s】二次打印内容为:【%s】", arg0, arg1));
		System.out.println(String.format("【%s】【%s】打印结束...", arg0,arg1));
	}
}
