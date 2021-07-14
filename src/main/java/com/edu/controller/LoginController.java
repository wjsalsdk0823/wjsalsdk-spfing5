package com.edu.controller;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.service.IF_MemberService;
import com.edu.util.NaverLoginController;
import com.edu.vo.MemberVO;
import com.github.scribejava.core.model.OAuth2AccessToken;

/**
 * 이 클래스는 스프링시큐리티의 /login처리한 결과를 받아서 login_success
 *
 * @author wjsal
 *
 */
@Controller
public class LoginController {
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Inject
	private IF_MemberService memberService;
	@Inject
	private NaverLoginController naverLoginController;
	
	//네이버 인증체크 후 이동할 URL 콜백 처리
	@RequestMapping(value="/naver_callback", method={RequestMethod.GET,RequestMethod.POST})
	public String naver_callback(@RequestParam(required=false)String code,@RequestParam String state,HttpSession session, Model model,RedirectAttributes rdat) throws IOException, ParseException  {
		
		if(code == null) {
			rdat.addFlashAttribute("msgError", "네이버 인증처리를 취소했습니다.");
			return "redirect:/login_form";
		}
		OAuth2AccessToken oauthToken;
		oauthToken =  naverLoginController.getAccessToken(session,code,state);
		String profile = naverLoginController.getUserProfile(oauthToken);
		logger.info("디버그119" + profile.toString());
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(profile);
		JSONObject jsonObj = (JSONObject) obj;
		JSONObject response_obj = (JSONObject) jsonObj.get("response");//프로파일
		String status = (String) jsonObj.get("message");//인증성공여부확인 변수값
		//위 최종적으로 출력된 response_obj를 파싱 시작 아래
		String username = (String) response_obj.get("name");
		String useremail = (String) response_obj.get("email");
		
		if(status.equals("success")) {//네이버 인증처리 결과가 success
			
		List<SimpleGrantedAuthority> authorities = new ArrayList();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		//스프링 시큐리티 인증 강제로 추가 아래
		Authentication authentication = new UsernamePasswordAuthenticationToken(useremail,null,authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//로그인 세션 변수 생성 아래
		session.setAttribute("session_enabled", true);
		session.setAttribute("session_userid", useremail);
		session.setAttribute("session_levels", "ROLE_USER");
		session.setAttribute("session_username", username);
		session.setAttribute("session_login_type", "sns");
		rdat.addFlashAttribute("msg", "네이버 아이디 로그인");
		
		} else {
			rdat.addFlashAttribute("msgError","네이버 인증이 실패했습니다. 다시 로그인해주세요!");
			return "redirect:/login_form";
		}
		return "redirect:/";
	}
	//홈컨트롤러에 있던 로그인 폼을 네아로 로그인 URL 생성때문에 이동
	@RequestMapping(value="/login_form", method=RequestMethod.GET)
	public String login_form(Model model,HttpSession session) throws Exception {
		
		String naverAuthUrl = "";
		naverAuthUrl = naverLoginController.getAuthorizationUrl(session);
		model.addAttribute("url", naverAuthUrl);
		return "home/login";//.jsp생략
	}
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
