package com.nano.web.vo.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/13.
 */
public class SmsVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String telephone;

    private String content;

    private String templateId;
    
    private String templateName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}
