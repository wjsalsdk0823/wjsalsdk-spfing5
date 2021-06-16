package com.edu.service;

import java.util.List;

import com.edu.vo.BoardTypeVO;

/**
 * 이 인터페이스는 DAO클래스를 접근하는 서비스 입니다
 * @author 전민아
 *
 */
public interface IF_BoardTypeService {
	// CRUD 메서드 명세만 생셩(아래5개)
	public void deleteBoardType(String board_type) throws Exception;
	public void updateBoardType(BoardTypeVO boardTypeVO) throws Exception;
	public BoardTypeVO readBoardType(String board_type) throws Exception;
	public void insertBoardType(BoardTypeVO boardTypeVO) throws Exception;
	//BoardTypeVO 1개의 레코드를 저장한 클래스를 List<>형 묶어서 받습니다.
	public List<BoardTypeVO> selectBoardType() throws Exception;	
}
