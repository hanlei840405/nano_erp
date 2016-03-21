package com.nano.constant;

/**
 * Created by Administrator on 2015/6/7.
 */
public class Constant {
	public static final Integer SIZE_THRESHOLD = 4096;
	public static final Integer MAXTHREADCNT = 50;
	public static final String OPEN = "open";
	public static final String CLOSED = "closed";
	public static final String INIT = "init";
	public static final String TAOBAO_URL = "http://gw.api.taobao.com/router/rest";
	public static final String JINGDONG_URL = "https://api.jd.com/routerjson";
	public static final String DANGDANG_URL = "http://api.open.dangdang.com/openapi/rest?";
	public static final String MATCH_BARCODE = "[^(a-zA-Z0-9)]";
	public static final String FORMAT_SYSTEM_BARCODE = "{0}{1}{2}{3}{4}{5}{6}{7}";
	public static final String FORMAT_TAOBAO_SKU_OUTER_ID = "{6}{7}";
	public static final String FORMAT_JINGDONG_SKU_OUTER_ID = "{0}{1}{2}{3}{4}{5}-{6}{7}";
	public static final String FORMAT_DANGDANG_BARCODE = "{0}{1}{2}{3}{4}{5}{6}{7}";

	public static void main(String[] args) {
		final SingleTonService singleTonService = SingleTonService.getInstance();
		for (int i = 0; i < 100; i++) {
			final String arg = i + "";
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					SingleTon singleTon = SingleTon.getInstance();
					singleTon.setSingleTonService(singleTonService);
					singleTon.print(arg + "");
				}
			};
			Thread thead = new Thread(runnable);
			thead.start();
		}
	}
}
