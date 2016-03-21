package com.nano.domain.base;

import java.io.Serializable;

/**
 * 区域快递关系
 * 
 * @author HANLEI
 *
 */
public class RegionExpress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String expressId;

	private String regionId;

	private int priority = 0;

	public String getExpressId() {
		return expressId;
	}

	public void setExpressId(String expressId) {
		this.expressId = expressId;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
