package com.nano.web.vo.system;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RoleVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String code;
    private String name;
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

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
