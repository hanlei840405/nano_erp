package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

/**
 * 字典项
 * 
 * @author HANLEI
 *
 */
public class DictionaryItem extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dictionaryId;

	private String code;

	private String name;

	public String getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(String dictionaryId) {
		this.dictionaryId = dictionaryId;
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
}
