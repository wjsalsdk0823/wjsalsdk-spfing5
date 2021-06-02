package com.edu.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

	@Test
	public void junitTest() {
		// 로거 장점 :조건에 따라서 출력을 조정할수있음
		logger.debug("Junit테스트시작입니다");
	}
}
