package com.nano.web.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/13.
 */
public class PageVO<T> {
    private long total;
    private List<T> rows = new ArrayList<>();

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }
}
