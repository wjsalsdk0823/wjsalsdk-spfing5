package com.edu.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.edu.service.IF_MemberService;
import com.edu.vo.MemberVO;

/**
 * 이 클래스는 오라클과 연동해서 CRUD를 테스트하는 클래스
 * 
 * @author 전민아
 *
 */
//RunWith 인터페이스는 현재 클래스가 JUnit실행클래스라고 명시
@RunWith(SpringJUnit4ClassRunner.class)
//경로에서**(모든폴더명시),*(모든파일명을명시)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/*.xml" })

@WebAppConfiguration
public class DataSourceTest {
	// 디버그용 로그 객체변수생성
	private Logger logger = Logger.getLogger(DataSourceTest.class);
	//dataSource 객체는 데이터베이스객체를 pool로 저장해서 사용할때  DataSource클래스를 사용(아래)
	@Inject //인젝트는 스프링에서 객체를 만드는 방법
	private DataSource dataSource;//Inject로 객체를 만들면 메모리 관리를 스프링이 대신해줌
	//Inject 자바8부터 지원, 이전 자바7에서@Autowired로 객체를 만듬
    @Inject//MemberService서비스를 주입받아서 객체를 사용(아래)
    private IF_MemberService memberService;
	
	//스프링 코딩 시작 순서
	@Test
	public void selectMember() throws Exception {
		//회원관리 테이블에서 더미로 입력한 100개의 레코드를 출력 메서드 테스트->회원관리 목록이 출력
		List<MemberVO> listMember = memberService.selectMember();
		listMember.toString();
	}
	
	@Test
	public void oldQueryTest() throws Exception {
		//스프링빈을 사용하지 않을때 예전 방식:코딩테스트에서는 스프링 설정을  x,직접DB아이디/암호 입력
		Connection connection = null;
		connection = 
	DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE","XE","apmsetup");
		logger.debug("데이터베이스 직접 접속이 성공 하였습니다. DB종류는 "+ connection.getMetaData().getDatabaseProductName());;
		//직접쿼리를 날립니다.날리기전 쿼리문장 객체생성 statement
		Statement stmt = connection.createStatement();
		//위 쿼리문장객체를 만드는 이유? 보안(SQL인젝션공격을 방지)
		//stmt객체가 없으면, 개발자가 SQL인젝션 방지코딩을 넣어야 합니다.
		//insert쿼리 문장 만듬(아래)
		//예전 방식으로 더미데이터
		/*
		 * for(int cnt=0;cnt<100;cnt++) {
		 * stmt.executeQuery("insert into dept02 values("+cnt+",'디자인부','경기도')"); }
		 */
		//sql디벨러퍼에서는 커밋이 필수, 외부java클래스 인서트할때는 자동커밋
		//테이블에 입력되어있는 레코드를 select 쿼리 stmt문장으로 가져옴 (아래 )
		ResultSet rs =stmt.executeQuery("select * from dept order by deptno ");
		//위에서 저장된 rs객체를 반복문을 출력(아래)
		while(rs.next()) {
			//rs객체의 레코드가 없을때까지 반복
			logger.debug(rs.getString("deptno")+" "+rs.getString("dname")+
					" "+rs.getString("loc"));
		}
		stmt = null;//메모리 반환
		rs = null;//메모리 반환
		connection = null;//메모리 초기화
	}
	@Test
	public void dbConnectionTest() {
		//데이터베이스 커넥션 테스트:설정은  root-context의 빈을 이용
		try {
			Connection connection = dataSource.getConnection();
			logger.debug("데이터베이스 접속이 성공 하였습니다. DB종류는 "+ connection.getMetaData().getDatabaseProductName());;
		} catch (SQLException e) {
			logger.debug("데이터베이스 접속이 실패 하였습니다.");
			//e.printStackTrace();
		}
		
		
	}
	@Test
	public void junitTest() {
		// 로거 장점 :조건에 따라서 출력을 조정할수있음
		logger.debug("Junit테스트시작입니다");
	}
}
