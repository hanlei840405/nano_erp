package com.nano.domain.productmapping;

import java.util.Date;

/**
 * Created by Administrator on 2015/9/26.
 */
public class DangDangProduct extends BaseProduct {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long platformProductId;
	private Long platformSkuId;
	private Date updateTime;

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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
