package com.nano.web.vo.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/13.
 */
public class WarehouseVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String name;

    private String code;

    private String status;

    private List<String> brandIds = new ArrayList<>();

    private List<String> brandNames = new ArrayList<>();

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

    public List<String> getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(List<String> brandIds) {
        this.brandIds = brandIds;
    }

	public List<String> getBrandNames() {
		return brandNames;
	}

	public void setBrandNames(List<String> brandNames) {
		this.brandNames = brandNames;
	}
}
