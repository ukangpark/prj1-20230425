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

	public boolean remove(Member member) {
		Member oldMember = mapper.selectById(member.getId());//기존정보를 받아와서
		
		int cnt = 0;
		
		if (oldMember.getPassword().equals(member.getPassword())) {
			//암호가 같으면?
			cnt = mapper.removeById(member.getId()); //mapper에게 넘겨서 delete하면됨
			
		} 
		
		return cnt == 1;
		
		
		
	}

	public void modify(String id) {
		mapper.selectById(id);
	}

	public boolean modify(Member member, String oldPassword) {
		Member oldMember = mapper.selectById(member.getId());
		
		int cnt = 0;
		if (oldMember.getPassword().equals(member.getPassword())) {
			cnt = mapper.update(member);
		}
		return cnt == 1;
	}

}
