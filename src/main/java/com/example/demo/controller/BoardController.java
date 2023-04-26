package com.example.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;
import com.example.demo.service.*;

@Controller
@RequestMapping("/")
public class BoardController {
	
	@Autowired //스프링빈을 사용할 것이다.
	private BoardService service; //서비스 객체를 주입받아서 서비스에게 일을 시키겠다.
	
	//경로 : http://localhost:8080으로와도 메소드가 일할테고
	//경로 : http://localhost:8080/list으로와도 메소드가 일할것
	//게시물 목록
	//@RequestMapping(path={"/", "list"}, method=RequestMethod.GET)
	@GetMapping({"/", "list"})
	public String list(Model model) {
		//1. request param 수집/가공
		//2. business logic 처리
		List<Board> list = service.listBoard();
		//3. add attribute
		model.addAttribute("boardList",list);
		
		//4. forward / redirect 
		return "list";
	}
	
	@GetMapping("/id/{id}")
	public String board(@PathVariable("id") Integer id, Model model) {
		//1. request param 수집/가공
		//2. business logic 처리 service에게 일을시킴
		Board board = service.getBoard(id);
		//3. add attribute
		model.addAttribute("board",board);
		//4. forward / redirect 
		return "get";
	}
	
	//수정전 조회하는일
	@GetMapping("/modify/{id}")
	public String modify(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("board", service.getBoard(id));
		return "modify";
	}
	
	//@RequestMapping(value = "/modify/{id}", method=RequestMethod=POST)
	@PostMapping("/modify/{id}")
	public String modifyProcess(Board board, RedirectAttributes rttr) {
		boolean ok = service.modify(board);//서비스에게 넘김
		//DB가 수정되고 만약 잘 수정되었다면(true)이면 다음 실행흐름을 이어가라
		
		if (ok) {
			//해당 게시물보기로 돌아가게 만들래 (리다이렉션)
			rttr.addAttribute("success", "success");
			return "redirect:/id/" + board.getId();
		} else {
			//잘못수정이 되었을 때는 수정페이지로 돌아가게 만들래
			rttr.addAttribute("fail", "fail");
			return "redirect:/modify/" + board.getId();
		}
		
	}
	
	@RequestMapping("remove")
	public String remove(Integer id, RedirectAttributes rttr) {
		boolean ok = service.remove(id);
		if (ok) {
			//전체 게시물보기로 돌아가게 만들래
			rttr.addAttribute("success", "remove");
			return "redirect:/list";
		} else {
			// 남아있게 할래
			return "redirect:/id/" + id;
		}
	}
	
}