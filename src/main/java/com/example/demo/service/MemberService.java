package com.example.demo.service;

import java.util.*;

import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class MemberService {
	
	@Autowired
	private CommentMapper commentMapper;
	
	@Autowired
	private MemberMapper mapper;
	
	@Autowired
	private BoardLikeMapper likeMapper;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean signup(Member member) {
		String plain = member.getPassword();
		member.setPassword(passwordEncoder.encode(plain));
		
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
		
		if (passwordEncoder.matches(member.getPassword(), oldMember.getPassword())) {
			//암호가 같으면?
			
			
			//이 회원이 작성한 댓글 삭제
			commentMapper.deleteByMemberId(member.getId());
			
			//이 회원이 작성한 게시물 row 삭제
			boardService.removeByWriter(member.getId());
			
			//이 회원이 누른 하트 레코드 삭제
			likeMapper.deleteByMemberId(member.getId());
			
			//회원 테이블 삭제
			cnt = mapper.removeById(member.getId()); //mapper에게 넘겨서 delete하면됨
		} 
		
		return cnt == 1;
		
		
		
	}

	public void modify(String id) {
		mapper.selectById(id);
	}

	public boolean modify(Member member, String oldPassword) {
		
		// 패스워드를 바꾸기 위해 입력했다면..
		if (!member.getPassword().isBlank()) {
			// 입력된 패스워드를 암호화
			String plain = member.getPassword();
			member.setPassword(passwordEncoder.encode(plain));
		}
		Member oldMember = mapper.selectById(member.getId());
		
		int cnt = 0;
		if (passwordEncoder.matches(oldPassword, oldMember.getPassword())) {
			cnt = mapper.update(member);
		}
		return cnt == 1;
	}

	public Map<String, Object> checkId(String id) {
		Member member = mapper.selectById(id);
		
		return Map.of("available", member == null); //null이면 true니까 id를 사용가능한것
	}

	public Map<String, Object> checkNickName(String nickName, Authentication auth) {
		Member member = mapper.selectByNickName(nickName);
		if (auth != null) {
			Member oldMember = mapper.selectById(auth.getName());
			return Map.of("available", member == null || oldMember.getNickName().equals(nickName));
			
		} else {
			return Map.of("available", member == null);
			
		}
	}

	public Map<String, Object> checkEmail(String email, Authentication auth) {
		Member member = mapper.selectByEmail(email);
		if (auth != null) {
			Member oldMember = mapper.selectById(auth.getName());
			return Map.of("available", member == null || oldMember.getEmail().equals(member.getEmail()));
		}
		return Map.of("available", member == null);
	}


}
