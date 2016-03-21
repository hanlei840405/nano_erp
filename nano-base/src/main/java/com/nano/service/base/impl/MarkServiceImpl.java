package com.nano.service.base.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Mark;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.MarkMapper;
import com.nano.service.base.MarkService;

@Service
public class MarkServiceImpl implements MarkService {

	@Autowired
	private MarkMapper markMapper;

	@Transactional
	public Mark save(Mark mark) {
		markMapper.saveOne(mark);
		return mark;
	}

	@Transactional
	public List<Mark> save(List<Mark> marks) {
		markMapper.saveMany(marks);
		return marks;
	}

	@Transactional
	public void delete(String id) {
		Mark mark = findOne(id);
		long resultCnt = markMapper.deleteOne(mark);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					mark.getId()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Mark findOne(String id) {
		return markMapper.findOne(id);
	}

	@Transactional
	public void update(Mark mark) {
		long resultCnt = markMapper.updateOne(mark);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					mark.getId()));
		}
	}

	@Transactional
	public void update(List<Mark> marks) {
		for (Mark mark : marks) {
			update(mark);
		}

	}

	public PageInfo<Mark> find(int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Mark> marks = markMapper.findMany(rowBounds);
		PageInfo<Mark> pageInfo = new PageInfo<Mark>(marks);
		return pageInfo;
	}

	public List<Mark> find() {
		return markMapper.findMany();
	}

}
