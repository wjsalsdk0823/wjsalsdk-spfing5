package com.edu.controller;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//외부 라이브러리(모듈) 사용 = import
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.service.IF_BoardService;
import com.edu.service.IF_MemberService;
import com.edu.util.CommonUtil;
import com.edu.vo.AttachVO;
import com.edu.vo.BoardVO;
import com.edu.vo.MemberVO;
import com.edu.vo.PageVO;


/**
 * 이 클래스는 MVC웹프로젝트를 최초로 생성시 자동으로 생성되는 클래스
 * 웹브라우저의 요청사랑을  view단(jsp)에 연결시켜주는 클래스 @Controller.
 * 스프링에서 관리하는 클래스를 스프링빈(콩) = 스프링빈을 명시@Controller 에노테이션
 * Beans(콩들) 그래프로 이 프로젝트의 규모를 확인가능
 * 스프링이 관리하는 클래스=스프링빈은  파일아이콘에 S가 붙습니다.
 */

@Controller
public class HomeController {
	//스프링빈(클래스)에서는 로거로 디버그를 합니다.= 로거객채
	//로그중 slf4j(Spring Log For Java)
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * 사용자요청(웹브라우저)을 받아서=@RdquestMapping 인터페이스를 사용해서 메서드명을 스프링이 구현
	 * ,router(루트rootx)
	 * return 값으로 view(jsp)를 선택해서 작업한 결과를 변수로 담아서 화면에 전송 후 결과를 표시 (렌더링) 합니다.
	 * 폼 전송시 post(자료숨김), get(자료노출)
	 */
	
	
	@Inject
	private IF_MemberService memberService;
	@Autowired
	private IF_BoardService boardService;
	@Inject
	private CommonUtil commonUtil;
	//@RequestMapping
	//public 뷰단 jsp파일명리턴형식 콜백함수
	//return 파일명
	
	//게시물 삭제 처리 호출 POST추가
	@RequestMapping(value="/home/board/board_delete",method=RequestMethod.POST)
	public String board_delete(@RequestParam("bno")Integer bno,RedirectAttributes rdat) throws Exception{
		//
		List<AttachVO> delFiles = boardService.readAttach(bno);
		//테이블 1개 레코드 삭제처리	
		boardService.deleteBoard(bno);
		//첨부파일 있으면 삭제
		for(AttachVO file:delFiles) {
			File target = null;
		} 
		rdat.addFlashAttribute("msg", "게시물 삭제");
		return "redirect:/home/board/board_list";
	}
	//게시물 상세보기 호출 GET
	@RequestMapping(value="/home/board/board_view",method=RequestMethod.GET)
	public String board_view(Model model,@RequestParam("bno")Integer bno,@ModelAttribute("pageVO")PageVO pageVO) throws Exception {
		//첨부파일내용 가져오기
		List<AttachVO> listAttachVO = boardService.readAttach(bno);
		String[] save_file_names = new String[listAttachVO.size()];
		String[] real_file_names = new String[listAttachVO.size()];
		int index = 0;
		for(AttachVO file:listAttachVO) {
			save_file_names[index] = file.getSave_file_name();
			real_file_names[index] = file.getReal_file_name();
			index = index + 1;
		}
		BoardVO boardVO = boardService.readBoard(bno);
		boardVO.setSave_file_names(save_file_names);
		boardVO.setReal_file_names(real_file_names);	
		//db테이블 데이터 가져오기
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("checkImgArray", commonUtil.getCheckImgArray());
		return "home/board/board_view"; 
	}
	//게시물 등록처리 호출 POST추가
	@RequestMapping(value="/home/board/board_insert",method=RequestMethod.POST)
	public String board_insert(RedirectAttributes rdat,@RequestParam ("file")MultipartFile[] files,BoardVO boardVO) throws Exception {
		//첨부파일 처리
		String[] save_file_names = new String[files.length];
		String[] real_file_names = new String[files.length];
		int index = 0;
		for(MultipartFile file:files) {
			//첨부파일 존재하면 실행조건
			if(file.getOriginalFilename()!="") {
				real_file_names[index] = file.getOriginalFilename();
				save_file_names[index] = commonUtil.fileUpload(file);
			}
			index = index + 1;
		}
		//Attach 테이블 처리할 첨부파일 가상변수  값을 입력
		boardVO.setSave_file_names(save_file_names);
		boardVO.setReal_file_names(real_file_names);
		//
		String rawTitle = boardVO.getTitle();
		String rawContent = boardVO.getContent();
		boardVO.setTitle (commonUtil.unScript(rawTitle));
		boardVO.setContent(commonUtil.unScript(rawContent));
		//DB테이블 처리
		boardService.insertBoard(boardVO);
		rdat.addFlashAttribute("msg", "게시물등록");
		return "redirect:/home/board/board_list";
	}
	//게시물 등록 폼 호출 GET 추가
	@RequestMapping(value="/home/board/board_insert_form",method=RequestMethod.GET)
	public String board_insert_form() throws Exception {
		
		return "/home/board/board_insert";
	}
	//게시물 리스트 페이지 호출 GET 추가
		@RequestMapping(value="/home/board/board_list",method=RequestMethod.GET)
		public String board_list(@ModelAttribute("pageVO") PageVO pageVO, Model model) throws Exception {
			if(pageVO.getPage() == null) {
				pageVO.setPage(1);
			}
			//pageVO의 2개 필수로 입력해야지만 페이징처리가 가능
			pageVO.setQueryPerPageNum(5);
			pageVO.setPerPageNum(5);
			int totalCount = boardService.countBoard(pageVO);
			pageVO.setTotalCount(totalCount);
			List<BoardVO> boardList = boardService.selectBoard(pageVO);
			model.addAttribute("boardList", boardList);
			return "home/board/board_list";//.jps생략
		}
	//404파일 에러 처리 get 호출 추가
	@RequestMapping(value = "/home/error/error_404", method = RequestMethod.GET)
	public String error_404() {
		return "home/error/error_404";
	}
	//회원가입 처리 호출POST방식
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(MemberVO memberVO, RedirectAttributes rdat) throws Exception {
		//rawpassword암호를 스프링시큐리티로 인코딩 합니다 아래
		String rawPassword = memberVO.getUser_pw();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		memberVO.setUser_pw(passwordEncoder.encode(rawPassword));
		//사용자레벨은 UI단에서 보내는 값 무시하고 강제로 입력
		memberVO.setLevels("ROLE_USER");
		memberService.insertMember(memberVO);
		rdat.addFlashAttribute("msg", "회원가입");
		return "redirect:/login_form";//페이지 리다이렉트로 이동
	}
	//회원가입폼 호출Get방식
	@RequestMapping(value = "/join_form", method = RequestMethod.GET)
	public String join_form() throws Exception {
		
		return "home/join";
	}
	//마이페이지에서 회원탈퇴
	@RequestMapping(value = "/member/mypage_leave", method = RequestMethod.POST)
	public String mypage_leave(MemberVO memberVO, RedirectAttributes rdat) throws Exception {
		memberService.updateMember(memberVO);
		rdat.addFlashAttribute("msg","회원탈퇴");
		return "redirect:/logout";
	}
	
	//마이페이지 회원정보 수정 POST방식
	@RequestMapping(value = "/member/mypage", method = RequestMethod.POST)
	public String mypage(MemberVO memberVO, RedirectAttributes rdat) throws Exception {
		if(!memberVO.getUser_pw().isEmpty()) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String rawPassword = memberVO.getUser_pw();
			memberVO.setUser_pw(passwordEncoder.encode(rawPassword));
		}
		memberService.updateMember(memberVO);
		rdat.addFlashAttribute("msg","회원정보수정");//
		return "redirect:/member/mypage_form";
	}
	//마이페이지 폼호출 GET방식
	@RequestMapping(value = "/member/mypage_form", method = RequestMethod.GET)
	public String mypage_form(HttpServletRequest request, Model model) throws Exception {
		//로그인 한 사죵자 세션을 session_userid로 memberService의 readMember를 호출
		//jsp에서 발생된 세션을 가져오려고 하기 때문에 HttpServletRequest 객체가 사용
		HttpSession session = request.getSession();
		String user_id = (String) session.getAttribute("session_userid");
		model.addAttribute("memberVO", memberService.readMember(user_id));
		return "home/member/mypage";
	}
	//사용자단 로그인 GET 로그인POST처리는 컨트롤러에서 하지 않고 스프링시큐리티로 처리
	@RequestMapping(value = "/login_form", method = RequestMethod.GET)
	public String login_form() throws Exception {
		
		return "home/login";
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homepage(Model model) {//콜백메서드,자동실행.
		String jspVar = "@서비스(DB)에서 치리한 결과";
		model.addAttribute("jspObject", jspVar );
		logger.info("디버그 스프링로고사용: " + jspVar);//System.out 대신 logger객체를 사용
		//home.jsp파일로 자료를 전송(스프링)하는 기능 model 인터페이스 객체(스프링이처리)에 내용만 채움
		return "home/index";//확장자가 생략. jsp 생략	
		}
	
}
