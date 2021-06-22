package com.edu.aop;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.edu.service.IF_BoardTypeService;
import com.edu.vo.BoardTypeVO;
import com.edu.vo.BoardVO;
import com.edu.vo.PageVO;

/**
 * 이 클래스는 AOP기능중 @Aspect과 @ControllerAdvice 로 구현됩니다
 * @author 전민아
 *
 */

@Aspect
@ControllerAdvice
public class AspectAdvice {
	private Logger logger = LoggerFactory.getLogger(AspectAdvice.class);
	@Inject
	private IF_BoardTypeService boardTypeService;
	
	//세션이란 서버-클라이언트 구조상에서 클라이언트가 서버에 접속할때 서버에 발생되는 정보를 세션이라고 함(서버에 저장됨)
	//쿠키란 서버- 클라이언트 구조상에서 클라이언트가 서버에 접속할때 클라이언트에서 발생되는 정보를 쿠키라고함(Pc에 저장됨)
	//Aspect로 AOP를 구현할떄는 포인트컷 (Advice참건이 실행될 위치)이 필요합니다
	//@Around=@Before+@Arter = @Around(포인트 컷 전+후0*(...)모든메서드)
	@Around("execution(* com.edu.controller.*Controller.*(..))")
	public Object sessionManager(ProceedingJoinPoint pjp) throws Throwable {
		//board_type 변수값을 세션에 저장하려고함
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		//컨트롤러 클래스에서 매개변수로 받을값(board_type) < pageVO,boardVO
		PageVO pageVO = null;
		String board_type = null;
		//조인포인트리스트의 객체를 1개씩 뽑아냄
		for(Object object:pjp.getArgs()) {
			if(object instanceof PageVO) {
				pageVO = (PageVO) object;
				board_type = pageVO.getBoard_type();
			}			
		}
		if(request !=null) {//jsp에서 Get.Post있을때
			//세션값을 pageVO.board_type 값으로 저장 로직
			HttpSession session = request.getSession();
			if(board_type != null) {
				session.setAttribute("session_board_type", board_type);
			}
			if(session.getAttribute("session_board_type") != null) {
				board_type = (String) session.getAttribute("session_board_type");
				if(pageVO != null) {//set은 pageVO가 unll아닐 경우만 실행되도록 처리
					pageVO.setBoard_type(board_type);//검색목표달성:여기서 항상 값을 가져가도록 구현
				}
			}
			logger.info("디버그19: "+(String) session.getAttribute("session_board_type"));
		}
		//Aspect > 포인트컷 (around)> 조인포인트 (메서드) > 매개변수로 구현
		Object result = pjp.proceed();
		return result;
	}
	
	//이 메서드는 컨트롤러로 메서드가 실행 전에 값을 생성해서 model객체에 담아서 jsp로 보냄
	//위 컨트롤러 어드바이스를 이용해서 컨트롤러의 모든 메서드가 실행되기 전 호출만되면 아래 메서드 자동실행
	@ModelAttribute("listBoardTypeVO")
	public List<BoardTypeVO> listBoardTypeVO() throws Exception {
		
		return boardTypeService.selectBoardType();
	}
}
