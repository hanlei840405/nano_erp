package com.nano.web.vo.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nano.constant.Constant;

/**
 * Created by Administrator on 2015/9/12.
 */
public class CategoryVO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String code;
    private String name;
    private String parentId;
    private Date created;
    private String text;
    private String state = Constant.OPEN;
    private List<CategoryVO> children = new ArrayList<>();

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<CategoryVO> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryVO> children) {
        this.children = children;
    }
}
