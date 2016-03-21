package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Member;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface MemberMapper {

	Member findOne(String id) throws NanoDBSelectRuntimeException;

	List<Member> findMany(Member member) throws NanoDBSelectRuntimeException;

	List<Member> findMany(Member member,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Member member) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Member> members) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Member member) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Member member) throws NanoDBDeleteRuntimeException;
}
