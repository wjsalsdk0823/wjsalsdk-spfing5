package com.edu.dao;

import java.util.List;

import com.edu.vo.BoardTypeVO;

/**
 * 이 인터페이스는 게시판 생성 관리 쿼리를 접근하기 위한 명세서 파일입니다
 * @author 전민아
 *
 */
public interface IF_BoardTypeDAO {
	// CRUD 메서드 명세만 생셩(아래5개)
	public void deleteBoardType(String board_type) throws Exception;
	public void updateBoardType(BoardTypeVO boardTypeVO) throws Exception;
	public BoardTypeVO readBoardType(String board_type) throws Exception;
	public void insertBoardType(BoardTypeVO boardTypeVO) throws Exception;
	//BoardTypeVO 1개의 레코드를 저장한 클래스를 List<>형 묶어서 받습니다.
	public List<BoardTypeVO> selectBoardType() throws Exception;
}
