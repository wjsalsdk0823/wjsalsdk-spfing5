package com.edu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.edu.service.IF_ReplyService;
import com.edu.vo.PageVO;
import com.edu.vo.ReplyVO;

/**
 * 이 클래스는 RestFul 서비스의 Endpoint를 만드는 클래스
 * RestAPI서버 만드는 클래스
 * @author wjsal
 *
 */
@RestController
public class ReplyController {
	private Logger logger = LoggerFactory.getLogger(ReplyController.class);
	@Inject
	private IF_ReplyService replyService;
	
	@RequestMapping(value="reply/reply_delete/{bno}/{rno}", method=RequestMethod.DELETE)
	public ResponseEntity<String> reply_delete(@PathVariable("bno")Integer bno,@PathVariable("rno")Integer rno){
		ResponseEntity<String> result = null;
		ReplyVO replyVO = new ReplyVO();
		replyVO.setBno(bno);
		replyVO.setRno(rno);
		try {
			replyService.deleteReply(replyVO);
			result = new ResponseEntity<String>("success",HttpStatus.OK);
		} catch (Exception e) {
			result = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
	@RequestMapping(value="reply/reply_update", method=RequestMethod.PATCH)
	public ResponseEntity<String> reply_update(@RequestBody ReplyVO replyVO) {
		ResponseEntity<String> result = null;
		try {
			replyService.updateReply(replyVO);
			result = new ResponseEntity<String>("success",HttpStatus.OK);
		} catch (Exception e) {
			result = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
	//댓글등록
	@RequestMapping(value="/reply/reply_insert", method=RequestMethod.POST)
	public ResponseEntity<String> reply_write(@RequestBody ReplyVO replyVO) {
		//ResponseEntity == ResponsBody
		ResponseEntity<String> result = null;
		//개발자가 스프링에 예외를 throws하지않고, 직접처리 try~catch하는 목적:
		//Rest상태값(200,204,500등의 상황들)을 개발자가 보내줘야 하기 때문에...
		try {
			replyService.insertReply(replyVO);
			result = new ResponseEntity<String>("success",HttpStatus.OK);//객체 생성시 매개변수로 상태값 + 입력성공시 success라는 문자열도 보냄

		} catch (Exception e) {
			result = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return result;
	}
	@RequestMapping(value="/reply/reply_list/{bno}/{page}", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> reply_list
	(@PathVariable("bno") Integer bno,@PathVariable("page")Integer page) {
		//ResponseEntity는 일반 Controller클래스에서 사용했던 ResponseBody와 같은 역할
		//Json데이터형으로 자료를 반환		
		/*Map<String,Object> dummyMap1 = new HashMap<String,Object>();
		Map<String,Object> dummyMap2 = new HashMap<String,Object>();
		Map<String,Object> dummyMap3 = new HashMap<String,Object>();
		dummyMap1.put("rno",1);
		dummyMap1.put("reply_text", "댓글리스트1");
		dummyMap1.put("replyer", "admin");
		dummyMap1.put("bno",bno);
		
		dummyMap2.put("rno",2);
		dummyMap2.put("reply_text", "댓글리스트2");
		dummyMap2.put("replyer", "admin");
		dummyMap2.put("bno",bno);
		
		dummyMap3.put("rno",3);
		dummyMap3.put("reply_text", "댓글리스트3");
		dummyMap3.put("replyer", "admin");
		dummyMap3.put("bno",bno);
		
		List<Object> dummyMapList = new ArrayList<Object>();
		dummyMapList.add(0, dummyMap1);
		dummyMapList.add(1, dummyMap2);
		dummyMapList.add(2, dummyMap3);	
		*/
		ResponseEntity<Map<String,Object>> result = null;
		try {
		PageVO pageVO = new PageVO();
		pageVO.setPage(page);
		//댓글 페이징 처리(아래 3개는 필수)
		pageVO.setPerPageNum(5);
		pageVO.setQueryPerPageNum(5);
		pageVO.setTotalCount(replyService.countReply(bno));
		logger.info("여기까지");
		//더미데이터 대신에 DB데이터를 가져와서 사용			
		//---------------------------------------------------------------		
		//아래의json데이터 형태는 일반컨트롤러에서 사용햇던 model사용해서 ("변수명,객체내용)전송한 방식과동일
		if(pageVO.getTotalCount() > 0) {
			//아래 resultMap을 만든 목적:위list객체를 responseEntity객체의 매개변수로 사용	
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("replyList", 	replyService.selectReply(bno, pageVO));
			resultMap.put("pageVO", pageVO);
		
			result = new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
		} else {
			result = new ResponseEntity<Map<String,Object>>(HttpStatus.NO_CONTENT);
		}
		//==================================================================
		}catch (Exception e) {
			result = new ResponseEntity<Map<String,Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return result; 
	}
}
