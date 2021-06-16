package com.edu.vo;
/**
 * 이 클래스는 게시판 생성관리의 데이터를 입/출력(임시저장)하는 기능의 클래스
 * 이 클래스를 이용해서 AOP(관점지향
 * @author 전민아
 *
 */
public class BoardTypeVO {
	//멤버변수 생성
	private String board_type;//PK고유값=식별자
	private String board_name;
	private Integer board_sun;//int>Integer null을 허용 nullPoint예외 처리 방지 가능
	//입출력가능한메서드를 만
	public String getBoard_type() {
		return board_type;
	}
	public void setBoard_type(String board_type) {
		this.board_type = board_type;
	}
	public String getBoard_name() {
		return board_name;
	}
	public void setBoard_name(String board_name) {
		this.board_name = board_name;
	}
	public Integer getBoard_sun() {
		return board_sun;
	}
	public void setBoard_sun(Integer board_sun) {
		this.board_sun = board_sun;
	}
	
}
