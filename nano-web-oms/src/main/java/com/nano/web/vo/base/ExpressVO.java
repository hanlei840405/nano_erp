package com.nano.web.vo.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/13.
 */
public class ExpressVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String code;

	private String telephone;

	private String contact;

	private String status;

	private String text;

	private List<String> platformIds = new ArrayList<>();

	private List<String> regionIds = new ArrayList<>();

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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getPlatformIds() {
		return platformIds;
	}

	public void setPlatformIds(List<String> platformIds) {
		this.platformIds = platformIds;
	}

	public List<String> getRegionIds() {
		return regionIds;
	}

	public void setRegionIds(List<String> regionIds) {
		this.regionIds = regionIds;
	}
}
