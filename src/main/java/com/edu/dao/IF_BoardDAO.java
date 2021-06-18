package com.edu.dao;

import java.util.List;

import com.edu.vo.AttachVO;
import com.edu.vo.BoardVO;
import com.edu.vo.PageVO;

/**
 * 이 인터페이스는 boardMapper.xml DataAccessObject 입니다
 * DAO와 서비를 나눈 이유 확인
 * @author 전민아
 *
 */
public interface IF_BoardDAO {
	//첨부파일CRUD아래
	public void deleteAttachAll(Integer bno) throws Exception;
	public void deleteAttach(String save_file_name) throws Exception;
	public void updateAttach(AttachVO attachVO) throws Exception;
	public void insertAttach(AttachVO attachVO) throws Exception;
	public List<AttachVO> readAttach(Integer bno) throws Exception;
	//상세보기시 조회수 올리는  메서드(아래)
	public void updateViewCount(Integer bno) throws Exception;
	//페이징 없는 검색된(board_type포함된)게시물 개수
	public int countBoard(PageVO pageVO) throws Exception;
	//기본CRUD아래
	public void deleteBoard(int bno) throws Exception;
	public void updateBoard(BoardVO boardVO) throws Exception;
	public BoardVO readBoard(int bno) throws Exception;
	public int insertBoard(BoardVO boardVO) throws Exception;
	public List<BoardVO> selectBoard(PageVO pageVO) throws Exception;
}
