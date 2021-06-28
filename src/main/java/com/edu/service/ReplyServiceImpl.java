package com.edu.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.dao.IF_ReplyDAO;
import com.edu.vo.PageVO;
import com.edu.vo.ReplyVO;

/**
 * 댓글 DAO의 CRUD 실행하는 서비스 클래스
 * @author User
 *
 */
@Service
public class ReplyServiceImpl implements IF_ReplyService {
	@Inject
	private IF_ReplyDAO replyDAO;
	
	@Transactional
	@Override
	public void deleteReply(ReplyVO replyVO) throws Exception {
		// TODO Auto-generated method stub
		replyDAO.deleteReply(replyVO);
		replyDAO.replyCountUpdate(replyVO.getBno(), -1);
	}

	@Override
	public void updateReply(ReplyVO replyVO) throws Exception {
		// TODO Auto-generated method stub
		replyDAO.updateReply(replyVO);
	}

	@Transactional //All or notall
	@Override
	public void insertReply(ReplyVO replyVO) throws Exception {
		// TODO Auto-generated method stub
		replyDAO.insertReply(replyVO);
		replyDAO.replyCountUpdate(replyVO.getBno(), 1);
	}

	@Override
	public int countReply(Integer bno) throws Exception {
		// TODO Auto-generated method stub
		return replyDAO.countReply(bno);
	}

	@Override
	public List<ReplyVO> selectReply(Integer bno, PageVO pageVO) throws Exception {
		// TODO Auto-generated method stub
		return replyDAO.selectReply(bno, pageVO);
	}

}
