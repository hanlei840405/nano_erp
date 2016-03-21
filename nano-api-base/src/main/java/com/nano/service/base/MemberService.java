package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Member;

public interface MemberService {

	Member save(Member member);

	List<Member> save(List<Member> members);

	void delete(String id);

	void delete(List<String> ids);

	Member findOne(String id);

	void update(Member member);

	void update(List<Member> members);

	PageInfo<Member> find(Member member, int pageNo, int pageSize);

	List<Member> find(Member member);
}
