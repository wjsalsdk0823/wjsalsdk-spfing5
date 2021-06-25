package com.edu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.edu.vo.PageVO;
import com.edu.vo.ReplyVO;

/**
 * 이 클래스는 sqlSession템플릿을 이용해서 쿼리를 실행하는 클래스
 * @author User
 *
 */
@Repository
public class ReplyDAOImpl implements IF_ReplyDAO{
	@Inject
	private SqlSession sqlSession;
	
	@Override
	public void deleteReplyAll(Integer bno) throws Exception {
		// TODO 
		sqlSession.delete("replyMapper.deleteReplyAll", bno);
	}

	@Override
	public void deleteReply(ReplyVO replyVO) throws Exception {
		// TODO 
		sqlSession.delete("replyMapper.deleteReply",replyVO);
	}

	@Override
	public void updateReply(ReplyVO replyVO) throws Exception {
		// TODO 
		sqlSession.update("replyMapper.updateReply", replyVO);
	}

	@Override
	public void replyCountUpdate(Integer bno, int count) throws Exception {
		// TODO 
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("bno",bno);
		paramMap.put("count", count);
		sqlSession.update("replyMapper.replyCountUpdate", paramMap);
	}

	@Override
	public void insertReply(ReplyVO replyVO) throws Exception {
		// TODO 
		sqlSession.insert("replyMapper.insertReply", replyVO);
	}

	@Override
	public int countReply(Integer bno) throws Exception {
		// TODO
		return sqlSession.selectOne("replyMapper.countReply", bno);
	}

	@Override
	public List<ReplyVO> selectReply(PageVO pageVO) throws Exception {
		// TODO 
		return sqlSession.selectList("replyMapper.selectReply", pageVO);
	}
	
}
