package com.edu.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.edu.vo.AttachVO;
import com.edu.vo.BoardVO;
import com.edu.vo.PageVO;

/**
 * 이 클래스는 게시물 CRUD를 구현하는 DAO클래스 입니다
 * @author 전민아
 *
 */
@Repository
public class BoardDAOImpl implements IF_BoardDAO{
	private Logger logger = LoggerFactory.getLogger(BoardDAOImpl.class);
	@Inject
	private SqlSession sqlSession;//sql세션템플릿에는insert,update
	@Override
	public void deleteAttachAll(Integer bno) throws Exception {
		// TODO sql세션템플릿을 상용한 매퍼쿼리
		sqlSession.delete("boardMapper.deleteAttachAll", bno);
	}

	@Override
	public void deleteAttach(String save_file_name) throws Exception {
		// TODO
		sqlSession.delete("boardMapper.deleteAttach", save_file_name);
	}

	@Override
	public void updateAttach(AttachVO attachVO) throws Exception {
		// TODO 
		sqlSession.insert("boardMapper.updateAttach",attachVO);
	}

	@Override
	public void insertAttach(AttachVO attachVO) throws Exception {
		// TODO 
		sqlSession.insert("boardMapper.insertAttach", attachVO);
	}

	@Override
	public List<AttachVO> readAttach(Integer bno) throws Exception {
		// TODO 
		return sqlSession.selectList("boardMapper.readAttach",bno);
	}

	@Override
	public void updateViewCount(Integer bno) throws Exception {
		// TODO 
		sqlSession.update("boardMapper.updatdeViewCount", bno);
	}

	@Override
	public int countBoard(PageVO pageVO) throws Exception {
		// TODO 
		return sqlSession.selectOne("boardMapper.countBoard", pageVO);
	}

	@Override
	public void deleteBoard(int bno) throws Exception {
		// TODO 
		sqlSession.delete("boardMapper.deleteBoard", bno);
	}

	@Override
	public void updateBoard(BoardVO boardVO) throws Exception {
		// TODO 
		sqlSession.update("boardMapper.updateBoard", boardVO);
	}

	@Override
	public BoardVO readBoard(int bno) throws Exception {
		// TODO 
		return sqlSession.selectOne("boardMapper.readBoard", bno);
	}

	@Override
	public int insertBoard(BoardVO boardVO) throws Exception {
		// TODO 아래 주석 동일+게시물 입력 후 반환값으로 bno를 받습니다.
		sqlSession.insert("boardMapper.insertBoard", boardVO);
		logger.info("디버그: " + boardVO.getBno());
		return boardVO.getBno();
	}

	@Override
	public List<BoardVO> selectBoard(PageVO pageVO) throws Exception {
		// TODO sqlSession 템블릿을 사용해서 매퍼쿼리 실생
		return sqlSession.selectList("boardMapper.selectBoard", pageVO);
	}

}
