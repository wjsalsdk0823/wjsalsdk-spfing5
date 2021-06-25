package com.edu.controller;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;

import com.edu.dao.IF_BoardDAO;
import com.edu.service.IF_BoardService;
import com.edu.service.IF_BoardTypeService;
import com.edu.service.IF_MemberService;
import com.edu.util.CommonUtil;
import com.edu.vo.AttachVO;
import com.edu.vo.BoardTypeVO;
import com.edu.vo.BoardVO;
import com.edu.vo.MemberVO;
import com.edu.vo.PageVO;

/**
 * 이 클래스는 Admin관리자단을 접근하는 클래스
 * 변수Object를 만들어서 jsp로 전송+ jsp 값을 받아서 Object로 처리
 * 디스패치 서블릿 실행될떄, 컨트롤러의 Request매핑경로를 재 등록합니다
 * @author 전민아
 *
 */
@Controller
public class AdminController {
	//컨트롤러수정하면 자동로딩(auto컴파일)
	//디버그용 로그객체 생성(아래)
	private Logger logger = LoggerFactory.getLogger(AdminController.class);
	//이 메서드는 회원목록을 출력하는 jsp와 매핑이 됩니다.
	@Inject
	private IF_MemberService memberService;
	@Inject
	private IF_BoardTypeService boardTypeService;
	@Inject
	private IF_BoardService boardService;
	@Inject
	private CommonUtil commonUtil;
	@Inject
	private IF_BoardDAO boardDAO;
	
	//게시물 등록을 POST로 처리합니다
	@RequestMapping(value="/admin/board/board_insert", method=RequestMethod.POST)
	public String board_insert(@RequestParam("file")MultipartFile[] files,BoardVO boardVO) throws Exception {
		//신규 등록이라서 기존 첨부파일 불러오는 로직은 필요없음.
		String[] save_file_names = new String[files.length];
		String[] real_file_names = new String[files.length];
		int index = 0;//첨부파일이 1개이상일때 반복변수로 사용
		for(MultipartFile file:files) {
			if(file.getOriginalFilename() != "") {//첨부파일이 있으면 실행
				save_file_names[index] = commonUtil.fileUpload(file);//물리적인파일저장
				real_file_names[index] = file.getOriginalFilename();//UI용 파일이름
			}
			index = index + 1;//index++
		}
		//신규등록 jsp폼에서 보낸 boardVO값에 아래 file에 대한 임시 변수값을 저장하는 로직
		boardVO.setSave_file_names(save_file_names);
		boardVO.setReal_file_names(real_file_names);
		boardService.insertBoard(boardVO);//DB에 저장하는 서비스 호출(실행)
		return "redirect:/admin/board/board_list";//게시판 테러방지용 redirect사용(새로고침시 무한등록을 방지)
		//게시판 신규등록시 자동으로 page가 1로 이동됩니다. 
	}
	//게시물 등록 폼을 GET으로 호출합니다
	@RequestMapping(value="/admin/board/board_insert_form", method=RequestMethod.GET)
	public String board_insert_form(@ModelAttribute("pageVO")PageVO pageVO) throws Exception {
		if(pageVO.getPage() == null) {
			pageVO.setPage(1);
		}
		return "admin/board/board_insert";
	}
	//게시물 수정처리는 Post로만 접근 가능
	@RequestMapping(value="/admin/board/board_update", method=RequestMethod.POST)
	public String board_updaer(@RequestParam("file")MultipartFile[] files, BoardVO boardVO, PageVO pageVO) throws Exception {
		//기존 등록된 첨부파일 목록 구하기List(2차원배열)객체의 크기는size()구함 기존파일이 있을때 사용
		List<AttachVO> delFiles = boardService.readAttach(boardVO.getBno());
		//1차원 배열의 크기는 length
		String[] save_file_names = new String[files.length];
		String[] real_file_names = new String[files.length];
		int index = 0;//jsp폼에서 보내온 파일에 대한 인덱스
		for(MultipartFile file:files) {
			if(file.getOriginalFilename() != "") {
				int sun = 0;
				for(AttachVO file_name:delFiles) {
					if(index == sun) {
						File target = new File(commonUtil.getUploadPath(),file_name.getSave_file_name());
						if(target.exists()) {
							target.delete();//뮬리적인 파일 지우는 명령
							//DB지우는 부분 추가
							boardDAO.deleteAttach(file_name.getSave_file_name());
						}//if(target.exists())
					}//if(idx == sun)
					sun = sun + 1;
				}//(AttachVO file_name:delFiles)
				//신규파일 업로드
				save_file_names[index] = commonUtil.fileUpload(file);
				real_file_names[index] = file.getOriginalFilename();
			}else{//if(file.getOriginalFilename() != "")
				save_file_names[index] = null;
				real_file_names[index] = null;		
			}
			index = index + 1;
		}//for(MultipartFile file:files) 
		boardVO.setSave_file_names(save_file_names);
		boardVO.setReal_file_names(real_file_names);
		//시큐어코딩추가(아래)
		String rawContent = boardVO.getContent();		
		String secContent = commonUtil.unScript(rawContent);
		boardVO.setContent(secContent);
		String rawTitle = boardVO.getTitle();		
		String secTitle = commonUtil.unScript(rawTitle);
		boardVO.setTitle(secTitle);
		//시큐어코딩 ㅋ끝
		boardService.updateBoard(boardVO);
		//첨부파일 작업전, 시큐어코딩:입력/수정 시큐어코딩적용
		
		String queryString = "bno="+boardVO.getBno()+"&page="+pageVO.getPage()+"&search_type="+pageVO.getSearch_type();
		return "redirect:/admin/board/board_view?"+queryString;
	}
	//게시물 수정 폼은 URL 쿼리스트링으로 접근
	@RequestMapping(value="/admin/board/board_update_form", method=RequestMethod.GET)
	public String board_update_form(Model model, @RequestParam("bno")Integer bno,
		@ModelAttribute("pageVO")PageVO pageVO) throws Exception {
		//첨부파일용save_file_names, real_file_names 2개 배열값을 구해서boardVO입력이 필요
		BoardVO boardVO = new BoardVO();
		boardVO = boardService.readBoard(bno);
		//여기서 첨부파일 배열을 추가(아래)
	
		List<AttachVO> listAttachVO = boardService.readAttach(bno);
		String[] save_file_names = new String[listAttachVO.size()];
		String[] real_file_names = new String[listAttachVO.size()];
		int idx = 0;
		for(AttachVO file_name:listAttachVO) {//세로데이터를 가로테이터로 변경하는 로직
			save_file_names[idx] = file_name.getSave_file_name();
			real_file_names[idx] = file_name.getReal_file_name();
			idx = idx + 1;
		}
		boardVO.setSave_file_names(save_file_names);
		boardVO.setReal_file_names(real_file_names);
		model.addAttribute("boardVO", boardVO);
		
		return "admin/board/board_update";
	}
	//게시물 삭제는 URL쿼리스트링으로 접근하지 않고, post방식을 처리
	@RequestMapping(value="/admin/board/board_delete",method=RequestMethod.POST)
	public String board_delete(@RequestParam("bno")Integer bno,PageVO pageVO) throws Exception {
		//디버그 삭제 할 전역변수 경로 확인
		logger.info("디버그 전역업로드경로: " + commonUtil.getUploadPath());
		//DB테블삭제한 이후 처뭅파일부터 있으면 삭제처리 .자바에서 파일핸들링처리
		//기존 등록된 처부파일 폴더에서 삭제할 UUID(고유한 식별자값 생성 클래스)이름을 추출합니다(아래)
		List<AttachVO> delFiles = boardService.readAttach(bno);		
		boardService.deleteBoard(bno);//첨부파일 테이블 삭제 후 게시물 테이블 삭제
		//물리적으로 파일 삭제 처리 시작
		for(AttachVO file_name:delFiles) {
			//File클래스는 ("파일의 업로드된 위치","삭제할 파일명");
			File target = new File(commonUtil.getUploadPath(),file_name.getSave_file_name());
			if(target.exists()) {
				target.delete();//뮬리적인 파일 지우는 명령
			}
		}
		
		String queryString = "page="+pageVO.getPage()+"&search_type"+pageVO.getSearch_type();
		return "redirect:/admin/board/board_list?"+queryString;
	}
	//게시물 상세보기 폼을 접근하지 않고URL쿼리 스트링으로 접근(get)
	@RequestMapping(value="/admin/board/board_view", method=RequestMethod.GET)
	public String board_view(@RequestParam("bno")Integer bno,@ModelAttribute("pageVO")PageVO pageVO, Model model) throws Exception {
		BoardVO boardVO =  boardService.readBoard(bno);
		
		//첨부파일 부분 attach데이터도 board_view.jsp로 이동해야함(아래)
		List<AttachVO> files = boardService.readAttach(bno);
		//개발자가 만든 클래스형 객체boardVO는 개발자가 만든 메서드 사용
		//반면, list<AttachVO> files List클래스형 객체 files는 내장형 메서드. =size()
		//배열 객체 생성 구조:String[] 배열명:new String[]배열크기;
		String[] save_file_names = new String[files.size()];
		String[] real_file_names = new String[files.size()];
		//attach테이블안의 해당 bno게시물의 첨부파일 이름 파싱해서 jsp로 보내주는 과정(아래)
		int cnt = 0;
		for(AttachVO file_name:files) {//files에서 1개의 레코드씩 추출
			save_file_names[cnt] = file_name.getSave_file_name();
			real_file_names[cnt] = file_name.getReal_file_name();
			cnt = cnt + 1;
		}
		//위 for은 세로데이터(다수레코드)를 가로데이터(1레코드이면,배열)담아서 1레코드 boardVo로 만든게 목적.
		boardVO.setSave_file_names(save_file_names);//파싱한 결과 set//다운로직에 필요
		boardVO.setReal_file_names(real_file_names);//boardVO에set//화면에 보이는데 필요
		model.addAttribute("boardVO", boardVO);
		//업로드한 파일이 이미지인지 아닌지 확인하는 용도의 테이터 입니다
		model.addAttribute("checkImgArray", commonUtil.getCheckImgArray());
		return "admin/board/board_view";
	}
	
	//게시물 목록은 URL로 접근하기 때문에 GET방식으로 처리
	@RequestMapping(value="/admin/board/board_list", method=RequestMethod.GET)
	public String board_list(@ModelAttribute("pageVO")PageVO pageVO, Model model) throws Exception {
		//게시판타입이 null일때 기본값으로 notice를 추가
		if(pageVO.getBoard_type() == null) {
			pageVO.setBoard_type("notice");
		}
		//페이징 처리를 위한 기본값 추가
		if (pageVO.getPage() == null) {
			 pageVO.setPage(1);
		}
		pageVO.setPerPageNum(5);//UI하단에서 보여줄 페이징 번호 크기		
		pageVO.setQueryPerPageNum(5);//토탈 카운트를 구하기전 2개의 값이 필수 (아래)
		pageVO.setTotalCount(boardService.countBoard(pageVO));
		
		model.addAttribute("listBoardVO", boardService.selectBoard(pageVO));
		return "admin/board/board_list";
	}
	//jsp에서 게시판 생성 관리에서 Get/post 접근할때 URL을 bbs_type로 지정합니다
	//board_type하지않고,bbs_type하는 이유는 왼쪽메뉴 고정시키는 로직에서 경로board와 겹치지 않도록
	@RequestMapping(value="/admin/bbs_type/bbs_type_list", method=RequestMethod.GET)
	public String selectBoardTypeList(Model model) throws Exception {//목록폼1
		//아래 모델은 AOP기능중 ControllerAdvice 인터페이스로 구현했기 때문에 아래는 주석
		//model.addAttribute("listBoardTypeVO",boardTypeService.selectBoardType());
		return "admin/bbs_type/bbs_type_list";//상대경로 view폴더가 root(최상위)
	}
	//bbs_type_list.jsp 게시판 생성 버튼을 클랙했을때 이동하는 폼 경로
	@RequestMapping(value="/admin/bbs_type/bbs_type_insert", method=RequestMethod.GET)
	public String insertBoardTypeFrom() throws Exception {//입력폼1
		
		return "admin/bbs_type/bbs_type_insert";
	}
	//bbs_type_insert.jsp의 입력폼 전송된 값을 boardTypeVO 담아서 구현={} 
	@RequestMapping(value="/admin/bbs_type/bbs_type_insert", method=RequestMethod.POST)
	public String insertBoardType(BoardTypeVO boardTypeVO) throws Exception {//입력처리1
		boardTypeService.insertBoardType(boardTypeVO);
		return "redirect:/admin/bbs_type/bbs_type_list";//절대경로
	}
	//게시판 생성관리는 이 기능은  사용자단에서 UI를 사용할 일이 없기때문에
	@RequestMapping(value="admin/bbs_type/bbs_type_update", method=RequestMethod.GET)
	public String updateBoardTypeForm(@RequestParam("board_type")String board_type, Model model) throws Exception {//수정폼1
		model.addAttribute("boardTypeVO", boardTypeService.readBoardType(board_type));//서식
		return "admin/bbs_type/bbs_type_update";//jsp생략
	}
	@RequestMapping(value="/admin/bbs_type/bbs_type_update", method=RequestMethod.POST)
	public String updateBoardType(BoardTypeVO boardTypeVO) throws Exception {//수정처리1
		boardTypeService.updateBoardType(boardTypeVO);
		return "redirect:/admin/bbs_type/bbs_type_update?board_type="+boardTypeVO.getBoard_type();
	}
	@RequestMapping(value="/admin/bbs_type/bbs_type_delete", method=RequestMethod.POST)
	public String deleteBoardType(@RequestParam("board_type")String board_type) throws Exception {//삭제처리1
		boardTypeService.deleteBoardType(board_type);
		return "redirect:/admin/bbs_type/bbs_type_list";//.jsp생략
	}
	
	//아래 경로는 신규등록 폼을 호출하는 URL쿼리스트링으로 보낸것을 받을때는 GET방식으로 받습니다.
	@RequestMapping(value="/admin/member/member_insert_form", method=RequestMethod.GET)
	public String insertMemberForm(@ModelAttribute("pageVO")PageVO pageVO) throws Exception {
		
		return "admin/member/member_insert";//jsp생략
	}
	//아래 경로는 회원신규등록을 처리하는 서비스를 호출하는 URL
	@RequestMapping(value="/admin/member/member_insert", method=RequestMethod.POST)
	public String insertMember(PageVO pageVO,MemberVO memberVO) throws Exception {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = memberVO.getUser_pw();//원시 패드워드값
		String encPassword = passwordEncoder.encode(rawPassword);
		memberVO.setUser_pw(encPassword);
		memberService.insertMember(memberVO);
		return "redirect:/admin/member/member_list";//.jsp생략
	}
	//아래 경로는 수정처리를 호출=DB를 변경처리함.
	@RequestMapping(value="/admin/member/member_update", method=RequestMethod.POST)
	public String updateMember(MemberVO memberVO, PageVO pageVO) throws Exception {
		//update 서비스만 처리하면 끝
		//업데이트 쿼리서비스 호출하기 전 스프링시큐리티 암호화 적용합니다.
		String rawPassword = memberVO.getUser_pw();
		if(!rawPassword.isEmpty()) {//수정폼에서 암호 입력값이 비어있지 않을때만 아래로직실행.
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encPassword = passwordEncoder.encode(rawPassword);
			memberVO.setUser_pw(encPassword);
		}
		memberService.updateMember(memberVO);//반환값이 없습니다.
		//redirect로 페이지를 이동하면, model로 담아서 보낼수 없습니다. 쿼리스트링(URL?)으로 보냅니다.
		String queryString = "user_id="+memberVO.getUser_id()+"&page="+pageVO.getPage()+"&search_type="+pageVO.getSearch_type();
		return "redirect:/admin/member/member_update_form?"+queryString;
	}
	//아래 경로는 수정폼을 호출=화면에 출력만=렌더링만 
	@RequestMapping(value="/admin/member/member_update_form", method=RequestMethod.GET)
	public String updateMemberForm(MemberVO memberVO, Model model,@ModelAttribute("pageVO")PageVO pageVO) throws Exception {
		//이 메서드는 수정폼에 pageVO, memberVO 2개의 데이터객체를 jsp로 보냅니다.
		//사용자1명의 레코드를 가져오는 멤버서비스(쿼리)를 실행(아래)
		MemberVO memberView = memberService.readMember(memberVO.getUser_id());
		//사용자1명의 레코드를 model에 담아서 + @ModelAttribute에 담아서 jsp로 보냅니다.
		model.addAttribute("memberVO", memberView);
		return "admin/member/member_update";//상대경로
	}
	@RequestMapping(value="/admin/member/member_delete", method=RequestMethod.POST)
	public String deleteMember(MemberVO memberVO) throws Exception {
		logger.info("디버그: " + memberVO.toString());
		//MemberVO memberVO는 클래스형 변수: String user_id 스트링형 변수 같은 방식.
		String user_id = memberVO.getUser_id();
		//이 메서드는 회원상세보기페이지에서 삭제버튼을 클릭시 전송받은 memberVO값을 이용해서 삭제를 구현(아래)
		memberService.deleteMember(user_id);//삭제쿼리가 실행됨
		//return "admin/member/member_list";//삭제후 이동할 jsp경로지정
		//위 방식대로하면, 새로고침하면, /admin/member/member_delete 계속 실행됩니다.-사용자단에서 실습
		//게시판테러상황을 방지하기 위해서, 쿼리를 작업 후 이동할때는 redirect(다시접속)라는 명령을 사용합니다.
		return "redirect:/admin/member/member_list";//단,redirect는 절대경로를 사용.
	}
	@RequestMapping(value="/admin/member/member_view", method=RequestMethod.GET)
	public String viewMemberForm(Model model, @RequestParam("user_id")String user_id, @ModelAttribute("pageVO")PageVO pageVO) throws Exception{
	//페이진입 시 받은 클래스변수값 PageVO pageVO
		/*
		 * 이 메서드는 리스트페이지에서 상세보기로 이동할때 보여주는 1개 레코드값을 보여주는 구현을 합니다.
		 * JUnit에서 테스트했던 readMember 방식을 이용.
		 * 다른점은 JUnit에서는 식별자 ID를 강제로 지정했지만, 이 메서드에서는 @RequsetParam인터페이스를 이용해서 식별자값을 받음.
		 */
		//위 출력값 memberVO 1개의 레코드를 model를 이용해서 member_view.jsp 보냅니다.(아래)
		model.addAttribute("memberVO", memberService.readMember(user_id));
		//model.addAttribute("pageVO", pageVO);
	//아래 페이지 반환시(렌더링) @ModelAttribute("pageVO")에 의해서 pageVO.page변수값으로 jsp보냅니다.
		return "admin/member/member_view";//상태경로 폴더파일위치
	}
	@RequestMapping(value="/admin/member/member_list", method=RequestMethod.GET)
	public String selectMember(@ModelAttribute("pageVO")PageVO pageVO,Model model) throws Exception {
		/*
		이 메서드는 2개 객체 생성하는 로직이 필요. 결과를 JSP로 보내는 기능을 수행
		1객체: memberList객체를 생성해서 model을 통해서 jsp로 전송 
		2객체: pageVO객체(prev,next,startPage,endPage)를 생성해서 model을 통해서 jsp로 전송
		2번객체부터 로직이 필요 -> memberList구하는 쿼리변수가 만들어지기 때문에 이것부터구현
		*/
		if(pageVO.getPage() == null) {//jsp에서 클릭값이 없을때만 초기값 입력
			pageVO.setPage(1);//초기값 1페이지 입력
		}
		//학습포인트: calcPage()로직(이해) < 변수(객체)값의 이동확인(코딩사용)
		pageVO.setQueryPerPageNum(5);//memberList객체+endPage구할때 필요
		pageVO.setPerPageNum(5);//startPage구할때-UI하단 페이지번호개수
		//위 2개의 변수값을 이용해서 아래 setTotalCount메서드에서 calcPage()호출됨
		pageVO.setTotalCount(memberService.countMember(pageVO));
		//calcPage 실행되면, prev, next변수 값이 입력됩니다.
		List<MemberVO> listMember = memberService.selectMember(pageVO);
		//위 setPerPageNum 20이면 next가 false(비활성화), 5이면 next가 true(활성화)
		logger.info("디버그" + pageVO.toString());
		//컨트롤러에서 jsp로 자료를 Model에 담아서 보내게 됩니다.
		model.addAttribute("listMember", listMember);
		//model.addAttribute("pageVO", pageVO);//나중에 @ModelAttribute로 대체
		return "admin/member/member_list";//jsp파일 상대경로
	}
	//URL요청 경로는 @RequestMapping 반드시 절대경로로 표시
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String admin(Model model) throws Exception {//에러발생시 Exception클래스의 정보를 스프링으로 보내게 됩니다.		
		//아래 상대경로에서 /WEB-INF/views/폴더가 루트(생략prefix접두어) 입니다.
		//아래 상대경로 home.jsp에서 .jsp (생략suffix접미어) 입니다.
		return "admin/home";//리턴 경로=접근경로는 반드시 상대경로로 표시
	}
}
