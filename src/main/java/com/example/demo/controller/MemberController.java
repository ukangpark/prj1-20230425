package com.example.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import com.example.demo.domain.*;
import com.example.demo.service.*;

@Controller
@RequestMapping("member")
public class MemberController {

	@Autowired
	private MemberService service;

	@GetMapping("signup")
	@PreAuthorize("isAnonymous()") // 로그인하지 않은 사람만 접근가능하게 할거야
	public void signupForm() {

	}
	
	@GetMapping("login")
	public void loginForm() {
		
	}

	@PostMapping("signup")
	@PreAuthorize("isAnonymous()") // 로그인하지 않은 사람만 접근가능하게 할거야
	public String signupProcess(Member member, RedirectAttributes rttr) {

		try {
			service.signup(member);
			rttr.addFlashAttribute("message", "회원 가입되었습니다.");
			return "redirect:/list";
		} catch (Exception e) {
			e.printStackTrace();
			rttr.addFlashAttribute("member", member);
			rttr.addFlashAttribute("message", "회원 가입 중 문제가 발생했습니다.");
			return "redirect:/member/signup";
		}
	}
	
	@GetMapping("memberlist")
	public void list(Model model) {
		List<Member> list = service.memberList();
		model.addAttribute("memberList", list);
	}
	
	@GetMapping("info")
	@PreAuthorize("isAuthenticated()") // 로그인한 사람만 접근권한을 줄거야.
	public void info(String id, Model model) {
		Member member = service.get(id);
		model.addAttribute("member", member);
	}
	
	@PostMapping("remove")
	@PreAuthorize("isAuthenticated()")
	public String remove(Member member, RedirectAttributes rttr) {
		boolean ok = service.remove(member);
		
		if (ok) {
			rttr.addFlashAttribute("message", "회원 탈퇴하였습니다.");
			return "redirect:/list";
		} else {
			rttr.addFlashAttribute("message", "회원 탈퇴시 문제가 발생했습니다.");
			return "redirect:/member/info?id=" + member.getId();
		}
	}
	
	@GetMapping("memberModify")
	@PreAuthorize("isAuthenticated()")
	public void memberModify(String id, Model model) {
		Member member = service.get(id);
		model.addAttribute("member", member);
		//model.addAttribute(service.get(id)); //attribute이름이 서비스를 가지는 객체타입이 lowcamel로 들어감
		
	}
	@PostMapping("memberModify")
	@PreAuthorize("isAuthenticated()")
	public String modifyProcess(Member member, String oldPassword, RedirectAttributes rttr) {
		System.out.println(member);
		boolean ok = service.modify(member, oldPassword);
		if(ok) {
			rttr.addFlashAttribute("message", "회원정보 수정이 완료되었습니다.");
			return "redirect:/member/info?id=" + member.getId();
		} else {
			rttr.addFlashAttribute("message", "회원정보 수정시 문제가 발생했습니다.");
			return "redirect:/member/memberModify?id=" + member.getId();
			
		}
	}


}
