package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Note;

public interface NoteService {

	Note save(Note note);

	List<Note> save(List<Note> notes);

	void delete(String id);

	void delete(List<String> ids);

	Note findOne(String id);

	void update(Note note);

	void update(List<Note> notes);

	PageInfo<Note> find(int pageNo, int pageSize);

	List<Note> find();
}
