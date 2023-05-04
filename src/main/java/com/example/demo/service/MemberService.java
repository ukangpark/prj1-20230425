package com.example.demo.service;

import java.util.*;

import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class MemberService {
	
	@Autowired
	private MemberMapper mapper;

	public Boolean signup(Member member) {
		int cnt = mapper.insert(member);
		return cnt == 1;
	}

	public List<Member> memberList() {
		List<Member> list = mapper.selectAll();
		return list;
	}

	public Member get(String id) {
		Member member = mapper.selectById(id);
		return member;
	}

}
