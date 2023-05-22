package com.example.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.*;
import com.example.demo.service.*;

//@Controller
@RestController
@RequestMapping("comment")
public class CommentController {

	@Autowired
	private CommentService service;
	
	@GetMapping("list")
	//@ResponseBody
	public List<Comment> list(@RequestParam("board") Integer boardId,
							Authentication auth) {
		return service.list(boardId, auth);
	}
	
	@PostMapping("add")
	//@ResponseBody
	//@PreAuthorize("authenticated")
	public ResponseEntity<Map<String, Object>> add(
											@RequestBody Comment comment,
											Authentication auth) {
		
		if (auth == null) {
			Map<String, Object> res = Map.of("message", "로그인 후 댓글을 작성해주세요");
			return ResponseEntity.status(401).body(res);
		} else {
			Map<String, Object> res = service.add(comment, auth);
			
			return ResponseEntity.ok().body(res);
			
		}
		
	}
	
	//@RequestMapping(path="id/{id}", method=RequestMethod.DELETE)
	@DeleteMapping("id/{id}")
	//@ResponseBody
	@PreAuthorize("authenticated and @customSecurityChecker.checkCommentWriter(authentication, #commentId)")
	public ResponseEntity<Map<String, Object>> deleteList(@PathVariable("id") Integer commentId) {
		Map<String, Object> res = service.deleteList(commentId);
		return ResponseEntity.ok().body(res);
	}
	
	//수정전 조회
	@GetMapping("id/{id}")
	//@ResponseBody
	public Comment get(@PathVariable("id") Integer id) {
		return service.get(id);
	}
	
	@PutMapping("update")
	//@ResponseBody
	@PreAuthorize("authenticated and @customSecurityChecker.checkCommentWriter(authentication, #id)")
	public ResponseEntity<Map<String, Object>> update(@RequestBody Comment comment, String id) {
		Map<String, Object> res = service.update(comment, id);
		return ResponseEntity.ok().body(res);
	}

	
}

