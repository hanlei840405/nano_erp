package com.nano.domain.productmapping;



/**
 * Created by Administrator on 2015/9/26.
 */
public class TaoBaoProduct extends BaseProduct {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long platformProductId;
	private Long platformSkuId;
	public Long getPlatformProductId() {
		return platformProductId;
	}
	public void setPlatformProductId(Long platformProductId) {
		this.platformProductId = platformProductId;
	}
	public Long getPlatformSkuId() {
		return platformSkuId;
	}
	public void setPlatformSkuId(Long platformSkuId) {
		this.platformSkuId = platformSkuId;
	}
}
