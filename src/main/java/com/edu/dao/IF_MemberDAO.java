package com.edu.dao;

import java.util.List;

import com.edu.vo.MemberVO;

/**
 * 이 인터페이스는 회원관리에 관련된 CRUD 메서드 명세 포함된 파일.
 * 이 인터페이스는 메서드명만 있고 {구현 내용}이 없는 목록파일.
 * @author 전민아
 *
 */
public interface IF_MemberDAO {
	//List<제네릭타입>:MemberVO 1개 레코드를 List클래스형 감싸주면
    //다수의 레코드를 저장할 수 가 있는 형태가 됩니다.
	public  List<MemberVO> selectMember() throws Exception ;
}
