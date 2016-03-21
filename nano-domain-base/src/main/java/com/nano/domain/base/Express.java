package com.nano.domain.base;

import java.util.ArrayList;
import java.util.List;

import com.nano.domain.AbstractEntity;

/**
 * 快递
 * 
 * @author HANLEI
 *
 */
public class Express extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String code;
	
	private String telephone;
	
	private String contact;
	
	private String status;
	
	private List<String> regionIds = new ArrayList<String>();

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

	public List<String> getRegionIds() {
		return regionIds;
	}

	public void setRegionIds(List<String> regionIds) {
		this.regionIds = regionIds;
	}
}
