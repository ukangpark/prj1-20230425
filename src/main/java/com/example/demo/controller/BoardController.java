package com.example.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
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
	public String list(Model model, 
					@RequestParam(value="page", defaultValue = "1") Integer page,
					@RequestParam(value="search", defaultValue = "") String search,
					@RequestParam(value="type", required = false)String type) {
		//1. request param 수집/가공
		//2. business logic 처리
		//List<Board> list = service.listBoard(); 
		Map<String, Object> result = service.listBoard(page, search, type);//파라미터가 있는 메소드를 새로만듦
		//3. add attribute
		//model.addAttribute("boardList",result.get("boardList"));
		//model.addAttribute("pageInfo",result.get("pageInfo"));
		model.addAllAttributes(result);
		
		//4. forward / redirect 
		return "list";
	}
	
	@GetMapping("/id/{id}")
	public String board(@PathVariable("id") Integer id, Model model) {
		//1. request param 수집/가공
		//2. business logic 처리 service에게 일을시킴
		Board board = service.getBoard(id);
		System.out.println(board);
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
			rttr.addFlashAttribute("message", board.getId() + "번 게시물이 수정되었습니다.");
			return "redirect:/id/" + board.getId();
		} else {
			//잘못수정이 되었을 때는 수정페이지로 돌아가게 만들래
			rttr.addFlashAttribute("message", board.getId() + "번 게시물이 수정되지 않았습니다.");
			//rttr.addAttribute("fail", "fail");
			return "redirect:/modify/" + board.getId();
		}
		
	}
	
	@PostMapping("remove")
	public String remove(Integer id, RedirectAttributes rttr) {
		boolean ok = service.remove(id);
		if (ok) {
			//query String에 추가
			//전체 게시물보기로 돌아가게 만들래
			//rttr.addAttribute("success", "remove");
			
			//모델에 추가
			rttr.addFlashAttribute("message", id + "번 게시물이 삭제되었습니다.");
			return "redirect:/list";
		} else {
			// 남아있게 할래
			return "redirect:/id/" + id;
		}
	}
	
	@GetMapping("add")
	public void addForm() {
		// 게시물 작성 form(view)로 포워드 
		
	}
	
	@PostMapping("add")
	public String addProcess(
						@RequestParam("files") MultipartFile[] files,
						Board board, RedirectAttributes rttr) throws Exception{
		//새 게시물 db에 추가
		boolean ok = service.insert(board, files);
		
		if (ok) {
			rttr.addAttribute("add", "add");
			return "redirect:/id/" + board.getId();
		} else {
			rttr.addFlashAttribute("board", board); //세션을 사용해서 잠깐 자바빈을 넣어둠
			return "redirect:/add";
		}
		
		
	}
}
