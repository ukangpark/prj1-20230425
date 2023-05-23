package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentService {
	
	@Autowired
	private CommentMapper mapper;

	public List<Comment> list(Integer boardId, Authentication auth) {
		
		List<Comment> comments = mapper.seletAllByBoardId(boardId);
		if (auth != null) {
			/*
			 * List<Comment> commentsWithEditable = comments.stream() .map(c -> {
			 * c.setEditable(c.getMemberId().equals(auth.getName())); return c; })
			 * .toList();
			 */
			for (Comment comment : comments) {
				comment.setEditable(comment.getMemberId().equals(auth.getName()));
			}
		}
		return comments;
	}

	public Map<String, Object> add(Comment comment, Authentication auth) {
		//comment.setMemberId("5");
		
		comment.setMemberId(auth.getName());
		
		var res = new HashMap<String, Object>();
		int cnt = mapper.insert(comment);
		if (cnt == 1) {
			res.put("message", "댓글이 등록되었습니다.");
		}else {
			res.put("message", "댓글이 등록되지 않았습니다.");
			
		}
		
		return res;
		
	}

	public Map<String, Object> deleteList(Integer commentId) {
		
		var res = new HashMap<String, Object>();
		
		int cnt = mapper.delete(commentId);
		
		if (cnt == 1) {
			res.put("message", "댓글이 삭제되었습니다.");
		} else {
			res.put("message", "댓글이 삭제되지 않았습니다.");
			
		}
		
		return res;
	}

	public Comment get(Integer id) {
		return mapper.selectById(id);
	}

	public Map<String, Object> update(Comment comment) {
		int cnt = mapper.update(comment);
		var res = new HashMap<String, Object>();
		if (cnt == 1) {
			res.put("message", "댓글이 수정되었습니다.");
		} else {
			res.put("message", "댓글이 수정되지 않았습니다.");
			
		}
		return res;
		
	}

}
