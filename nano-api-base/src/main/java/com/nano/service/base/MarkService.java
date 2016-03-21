package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Mark;

public interface MarkService {

	Mark save(Mark mark);

	List<Mark> save(List<Mark> marks);

	void delete(String id);

	void delete(List<String> ids);

	Mark findOne(String id);

	void update(Mark mark);

	void update(List<Mark> marks);

	PageInfo<Mark> find(int pageNo, int pageSize);

	List<Mark> find();
}
