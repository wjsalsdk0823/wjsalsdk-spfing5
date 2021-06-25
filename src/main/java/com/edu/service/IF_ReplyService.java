package com.edu.service;

import java.util.List;

import com.edu.vo.PageVO;
import com.edu.vo.ReplyVO;

/**
 * 인터페이스는 댓글 dao를 이용해서 댓글 CRUD를 구현하는 서비스
 * @author User
 *
 */
public interface IF_ReplyService {	 	
		public void deleteReply(ReplyVO replyVO) throws Exception;
		public void updateReply(ReplyVO replyVO) throws Exception;
		public void insertReply(ReplyVO replyVO) throws Exception;
		public int countReply(Integer bno) throws Exception;
		public List<ReplyVO> selectReply(PageVO pageVO) throws Exception;
	}

