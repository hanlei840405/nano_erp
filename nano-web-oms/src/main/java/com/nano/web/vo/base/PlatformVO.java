package com.nano.web.vo.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/13.
 */
public class PlatformVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String name;

    private String code;

    private String status;

    private List<String> expressIds = new ArrayList<>();

    private List<String> expressNames = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public List<String> getExpressIds() {
        return expressIds;
    }

    public void setExpressIds(List<String> expressIds) {
        this.expressIds = expressIds;
    }

	public List<String> getExpressNames() {
		return expressNames;
	}

	public void setExpressNames(List<String> expressNames) {
		this.expressNames = expressNames;
	}
}
