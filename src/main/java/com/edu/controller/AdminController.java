package com.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 이 클래스는 Admin관리자단을 접근하는 클래스
 * 변수Object를 만들어서 jsp로 전송+ jsp 값을 받아서 Object로 처리
 * @author 전민아
 *
 */
@Controller 
public class AdminController {
	 //URL요청 경로는 @RequestMapping 반드시 절대경로로 표시
     @RequestMapping(value="/admin", method=RequestMethod.GET)
     public String admin(Model model) throws Exception {//에러발생시
    	 
    	 //아래 상대경로에서 views 폴더가 루트(최상위)입니다
    	 //아래 상대경로home.jsp에서 .jsp (생략suffix접미어)입니다
    	 return "admin/home";//리턴경로=접근경로는 상대경로로 표시
     }
}
