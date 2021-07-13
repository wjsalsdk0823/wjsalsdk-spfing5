package com.edu.util;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;


@PropertySource("classpath:properties/sns.properties")
@Controller
public class NaverLoginController {
	@Value("${SnsClientID}")
	private String CLIENT_ID;
	@Value("${SnsClientSecret}")
	private String CLIENT_SECRET;
	@Value("${SnsCallbackUrl}")
	private String REDIRECT_URL;
	
	private final static String SESSION_STATE = "oauth_state";
	private final static String PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me\r\n"; 
			
	//네이버에서 제공하는 인증 URL구하는 메서드 (사용자 로그인폼에$url로 제공하게됨)
	public String getAuthorizationUrl (HttpSession session) {
		//세션에 유효성 검증을 위하여 난수를 생성 아래
		String state = generteRandomString();
		setSession(session,state);
		OAuth20Service oauthService = new ServiceBuilder()
				.apiKey(CLIENT_ID)
				.apiSecret(CLIENT_SECRET)
				.callback(REDIRECT_URL)
				.state(state)
				.build(NaverLoginApi.instance());
				
		return oauthService.getAuthorizationUrl();
	}

	private void setSession(HttpSession session, String state) {
		// TODO Auto-generated method stub
		session.setAttribute(SESSION_STATE, state);
	}

	private String generteRandomString() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString();
	}
}
