package com.edu.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.service.IF_MemberService;
import com.edu.vo.MemberVO;
import com.edu.vo.PageVO;

/**
 * 이 클래스는 Admin관리자단을 접근하는 클래스
 * 변수Object를 만들어서 jsp로 전송+ jsp 값을 받아서 Object로 처리
 * @author 전민아
 *
 */
@Controller 
public class AdminController {
	private Logger logger = LoggerFactory.getLogger(AdminController.class);
	 //이 메서드는 회원목록을 출력하는 jsp와 매핑이 됩니다.
	@Inject
	private IF_MemberService memberService;
	
	//아래 경로는 수정처리를 호출=DB를 변경처리함.
	@RequestMapping(value="/admin/member/member_update", method=RequestMethod.POST)
	public String updateMember(MemberVO memberVO, PageVO pageVO) throws Exception {
		//update 서비스만 처리하면 끝
		//업데이트 쿼리서비스 호출하기 전 스프링시큐리리티 암호화 적용합니다.
		String rawPassword = memberVO.getUser_pw();
		if(!rawPassword.isEmpty()) {//수정폼에서 암포 입력값이 비어있지 않을때만 아래로직 실행
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encPassword = passwordEncoder.encode(rawPassword);
			memberVO.setUser_pw(encPassword);
		}
		memberService.updateMember(memberVO);//반환 값이 없습니다.
		//redirect로 페이지를 이동하면,model로 담아서 보낼수 없습니다. 쿼리스트링(URL?)으로 보냅니다.
		String queryString = "user_id="+memberVO.getUser_id()+"&page="+pageVO.getPage()+"&search_type="+pageVO.getSearch_type
()+"&search_keyword"+pageVO.getSearch_keyword();
		return "redirect:/admin/member/member_update_form?"+queryString;//.jsp생략
	}
	
	//아래 경로는 수정폼을 호출=화면에 출력만=렌더링만
    @RequestMapping(value="/admin/member/member_update_form", method=RequestMethod.GET)
    public String updateMemberForm(MemberVO memberVO, Model            model,@ModelAttribute("pageVO")PageVO pageVO)  throws Exception {
	//사용자1명의 레코드를 가져오는 멤버서비스를 실행합니다 (아래)
	MemberVO memberView = memberService.readMember(memberVO.getUser_id());
	//사용자1명의 레코드를 model에 담아서 member_update.jsp로 보냄
	model.addAttribute("memberVO", memberView);
	return "admin/member/member_update";//상대경로
	}
	@RequestMapping(value="/admin/member/member_delete", method=RequestMethod.POST)
	public String deleteMember(MemberVO memberVO) throws Exception {
		logger.info("디버그: "+ memberVO.toString());
		//MemberVO memberVo 의미:String user_id 스트링형 변수 같은 방식
		String user_id = memberVO.getUser_id();
		//이 메서드는 회원상세보기페이지에서 삭제버튼을 클릭시 전송받은 memberVO값을 이용해서 삭제를 구현(아래)
		memberService.deleteMember(user_id);//삭제쿼리가 실행됨
		//return "admin.member/member_list";//삭제 후 이동할 jsp경로지정
		//위 방식대로하면, 새로고침 계속 실행됩니다
		
		//게시판테러상황을 방지하기 위해서, 쿼리를 작업 후 이동할때는 redirect(다시접속)라는 명령을 사용합니다.
		return "redirect:/admin/member/member_list"; //redirect는 절대경로를 사용
	}
	@RequestMapping(value="/admin/member/member_view", method=RequestMethod.GET)
	public String viewMemberForm(Model model,@RequestParam("user_id")String user_id,@ModelAttribute("pageVO")PageVO pageVO) throws Exception{
	//페이지 집입 시 받은 클래스 변수 값 PageVO pageVO	
		/*
		 *이 메서드는 리스트페이지에서 상세보기로 이동할때 보여주는 1개 레코드 값을 보여주는 구현
		 *JUnit에서 테스트했던 readMember방식을 이용
		 *다른점은 JUnti에서는 식별자 ID를 강제로 지정했지만, 이 메서드에서는 @RequserParam인터페이스를 이용해서 식별자 값을 받음.
		 */
		//위 출력 값 memberVO 1개의 레코드를 model를 이용해서 member_view.jsp보냅니다(아리_)
		model.addAttribute("memberVO", memberService.readMember(user_id));
		//아래 페이지 반환시(랜더링)@ModelAttribute("PageVO")에 의해서pageVO.page변수값으로 jsp보냅니다.
		return "admin/member/member_view";//상대경로 폴더파일위치
	}
	 @RequestMapping(value="/admin/member/member_list", method=RequestMethod.GET)
	 public String selectMember(@ModelAttribute("pageVO")PageVO pageVO,Model model) throws Exception{
		 /*
		  이 메서드는 2개 객체 생성하는 로직이 필요 jsp로 보내는 기능을 수행
		  1객체 memberList객체를 생성해서 model을 통해서 jsp로 전송
		  2객체 pageVO객체를 생성해서 model을 통해서 jsp로 전송 
		  2번 객체부터 로직이 필요 ->memberList구하는 쿼리변수가 만들어지기 때문에 이것부터 구현
		  */
		 if(pageVO.getPage() == null) {//jsp에서 클릭값이 없을때만 초기값 입력
			 pageVO.setPage(1);//초기값 1페이지 입력
		 }
		 //학습포인트:calcPage()로직<변수(객체)값의 이동확인
		 pageVO.setQueryPerPageNum(5);//startpage,endPage구할때 필요
		 pageVO.setPerPageNum(5);//startPage구할때	
		 //위 2개의 변수값을 이용해서 아래 setTotalCount메서드에서 calcPage()호출
		 pageVO.setTotalCount(memberService.countMember(pageVO));
		 //calcPage 실행되면, prev,next변수 값이 입력됩니다.
		 List<MemberVO> listMember = memberService.selectMember(pageVO);
		 //위 setPerPageNum 20이면 next가false(비활성화), 5이면next가 true(활성화)
		 logger.info("디버그" + pageVO.toString());
		 //컨트롤러에서 jsp로 자료는 Model에 담아서 보내
		 model.addAttribute("listMember", listMember);
		 //model.addAttribute("pageVO", pageVO);
		 return "admin/member/member_list";//jsp파일 상대경로
	 }
	 //URL요청 경로는 @RequestMapping 반드시 절대경로로 표시
     @RequestMapping(value="/admin", method=RequestMethod.GET)
     public String admin(Model model) throws Exception {//에러발생시
    	 
    	 //아래 상대경로에서 views 폴더가 루트(최상위)입니다
    	 //아래 상대경로home.jsp에서 .jsp (생략suffix접미어)입니다
    	 return "admin/home";//리턴경로=접근경로는 상대경로로 표시
     }
}
