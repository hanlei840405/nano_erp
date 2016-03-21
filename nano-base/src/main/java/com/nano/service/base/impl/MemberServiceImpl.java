package com.nano.service.base.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Member;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.MemberMapper;
import com.nano.service.base.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper memberMapper;

	@Transactional
	public Member save(Member member) {
		memberMapper.saveOne(member);
		return member;
	}

	@Transactional
	public List<Member> save(List<Member> members) {
		memberMapper.saveMany(members);
		return members;
	}

	@Transactional
	public void delete(String id) {
		Member member = findOne(id);
		long resultCnt = memberMapper.deleteOne(member);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					member.getId() + " : " + member.getNick()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		for(String id : ids){
			delete(id);
		}
	}

	public Member findOne(String id) {
		return memberMapper.findOne(id);
	}

	@Transactional
	public void update(Member member) {
		long resultCnt = memberMapper.updateOne(member);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					member.getId() + " : " + member.getNick()));
		}
	}

	@Transactional
	public void update(List<Member> members) {
		for(Member member : members){
			update(member);
		}

	}

	public PageInfo<Member> find(Member member, int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Member> members = memberMapper.findMany(member, rowBounds);
		PageInfo<Member> pageInfo = new PageInfo<Member>(members);
		return pageInfo;
	}

	public List<Member> find(Member member) {
		return memberMapper.findMany(member);
	}

}
