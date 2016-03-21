package com.nano.web.vo.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/13.
 */
public class DictionaryItemVO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String code;
    private String name;
    private String dictionaryId;

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

    public String getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(String dictionaryId) {
        this.dictionaryId = dictionaryId;
    }
}
