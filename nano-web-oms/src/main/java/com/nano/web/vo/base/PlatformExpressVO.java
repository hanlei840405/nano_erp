package com.nano.web.vo.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/13.
 */
public class PlatformExpressVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String platformId;

    private String expressId;

    private String code;

    private String alias;

    private String standardCode;

    private String name;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getStandardCode() {
        return standardCode;
    }

    public void setStandardCode(String standardCode) {
        this.standardCode = standardCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
