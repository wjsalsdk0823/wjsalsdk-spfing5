package com.edu.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
	 @RequestMapping(value="/admin/member/member_list", method=RequestMethod.GET)
	 public String selectMember(PageVO pageVO) throws Exception{
		 //jsp의 검색시 search_type, searh_keyword 내용이 PageVO클래스에 Set됩니다.
		 
		 //역방향 검색한 결과를 jsp 보내줍니다.(아래)
		 //위에서 검색어를 받아서 역방향 검색한 결과를 만들어서 jsp보내줍니다.(아래)
		 if(pageVO.getPage() == null) {//jsp에서 전송값이 없을때만 초기값 입력
			 pageVO.setPage(1);
		 }
		 //pageVO의 calcPage메서드를 실행하려면, 필수 변수값입력(아래)
		 pageVO.setTotalCount(memberService.countMember(pageVO));//결과의 전체 카운드 값
		 pageVO.setQueryPerPageNum(10);
		 pageVO.setPerPageNum(10);//하단 UI에 보여줄 페이지 개수
		 List<MemberVO> listMember = memberService.selectMember(pageVO);
		
		 logger.info("디버그" + pageVO.toString());//지금까지jsp->컨트롤러 일방향 자료 이동.
		 //컨트롤러에서 jsp로 역방향을 ㅗ보내는 자료는 Model에 담아서 보내
		 return "admin/member/member_list";//jsp파일 상대경로 /슬러시없으면 절대경로
	 }
	 //URL요청 경로는 @RequestMapping 반드시 절대경로로 표시
     @RequestMapping(value="/admin", method=RequestMethod.GET)
     public String admin(Model model) throws Exception {//에러발생시
    	 
    	 //아래 상대경로에서 views 폴더가 루트(최상위)입니다
    	 //아래 상대경로home.jsp에서 .jsp (생략suffix접미어)입니다
    	 return "admin/home";//리턴경로=접근경로는 상대경로로 표시
     }
}
