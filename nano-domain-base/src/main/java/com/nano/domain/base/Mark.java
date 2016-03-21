package com.nano.domain.base;

import com.nano.domain.AbstractEntity;

/**
 * 标记
 * 
 * @author HANLEI
 *
 */
public class Mark extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
