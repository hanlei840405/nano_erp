package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Note;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface NoteMapper {

	Note findOne(String id) throws NanoDBSelectRuntimeException;

	List<Note> findMany() throws NanoDBSelectRuntimeException;

	List<Note> findMany(RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Note note) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Note> notes) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Note note) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Note note) throws NanoDBDeleteRuntimeException;
}
