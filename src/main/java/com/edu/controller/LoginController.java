package com.edu.controller;

import java.util.Collection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.service.IF_MemberService;
import com.edu.vo.MemberVO;

/**
 * 이 클래스는 스프링시큐리티의 /login처리한 결과를 받아서 login_success
 *
 * @author wjsal
 *
 */
@Controller
public class LoginController {
	@Inject
	private IF_MemberService memberService;

	@RequestMapping(value="/login_success", method=RequestMethod.GET)
	public String login_success(HttpServletRequest request, RedirectAttributes rdat) throws Exception {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//싱글톤 객체를 만들거나, 사용하는 목적은 메모리 관리를 위해서 1개만 객체로 만들어서 사용하기 위해서(new로 객체를 생성하지 못함)
		String userid = "";
		String levels = "";
		Boolean enabled = false;
		Object principal = authentication.getPrincipal();//아이디,암호 비교쿼리가 실행됩니다.
		if(principal instanceof UserDetails) {
			enabled = ((UserDetails)principal).isEnabled();
			//위 인증결과로 로그인 체크를 합니다
		}
		//로그인 인증이 true라면 내용 실행(세션값-로그인 아이디,권한,회원이름 저장하는 목적
		if(enabled) {
			HttpSession session = request.getSession();
			//자바8이상에서 지원되는 람다식 사용 아래
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ANONYN")).findAny().isPresent()) {
				levels = "ROLE_ANONYMOUS";
			}
			if(authorities.stream().filter(o -> o.getAuthority().equals("ROLE_USER")).findAny().isPresent() ) {
				levels = "ROLE_USER";
			}
			if( authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent()) {
				levels = "ROLE_ADMIN";
			}
			//람다식은 외국코드 분석할때 필요
			userid = ((UserDetails) principal).getUsername();
			//위에서 구한 변수 3개 enabled, levels,userid를 세션변수로 저장 아래
			session.setAttribute("session_enabled", enabled);
			session.setAttribute("session_levels", levels);
			session.setAttribute("session_userid", userid);
			MemberVO memberVO = memberService.readMember(userid);
			session.setAttribute("session_username", memberVO.getUser_name());
		}
		rdat.addFlashAttribute("msg","로그인");	
		return "redirect:/";
	}
}
