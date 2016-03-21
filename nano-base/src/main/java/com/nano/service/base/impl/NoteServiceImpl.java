package com.nano.service.base.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Note;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.NoteMapper;
import com.nano.service.base.NoteService;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NoteMapper noteMapper;

	@Transactional	
	public Note save(Note note) {
		noteMapper.saveOne(note);
		return note;
	}

	@Transactional
	public List<Note> save(List<Note> notes) {
		noteMapper.saveMany(notes);
		return notes;
	}

	@Transactional
	public void delete(String id) {
		Note note = findOne(id);
		long resultCnt = noteMapper.deleteOne(note);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					note.getId()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Note findOne(String id) {
		return noteMapper.findOne(id);
	}

	@Transactional
	public void update(Note note) {
		long resultCnt = noteMapper.updateOne(note);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					note.getId()));
		}
	}

	@Transactional
	public void update(List<Note> notes) {
		for (Note note : notes) {
			update(note);
		}

	}

	public PageInfo<Note> find(int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Note> notes = noteMapper.findMany(rowBounds);
		PageInfo<Note> pageInfo = new PageInfo<Note>(notes);
		return pageInfo;
	}

	public List<Note> find() {
		return noteMapper.findMany();
	}

}
