package com.nano.domain.base;

import java.io.Serializable;

/**
 * 平台快递关系
 * 
 * @author HANLEI
 *
 */
public class PlatformExpress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String platformId;

	private String expressId;

	private String alias;

	private String code;

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getExpressId() {
		return expressId;
	}

	public void setExpressId(String expressId) {
		this.expressId = expressId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
