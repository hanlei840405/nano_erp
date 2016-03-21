package com.nano.web.vo.system;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/22.
 */
public class PrivilegeVO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String code;
    private String name;
    private String category;
    private String[] relatedIds;
    private String relatedName;
    private Map<String,Object> attributes = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getRelatedIds() {
		return relatedIds;
	}

	public void setRelatedIds(String[] relatedIds) {
		this.relatedIds = relatedIds;
	}

	public String getRelatedName() {
        return relatedName;
    }

    public void setRelatedName(String relatedName) {
        this.relatedName = relatedName;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
