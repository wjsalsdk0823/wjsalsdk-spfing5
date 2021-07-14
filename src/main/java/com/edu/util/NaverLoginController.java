package com.edu.util;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth.OAuthService;


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
	private final static String PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me"; 
			
	
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
		// 세션 유효성 검증을 위한 난수 생성기
		return UUID.randomUUID().toString();
	}

	public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException {
		//네이버 인증 RestApi에서 인증 데이터 토큰 값 가져오기
		//콜백 URL로 전달 받은 세션 검증용 난수 값
		String sessionState = getSession(session);
		if(StringUtils.pathEquals(sessionState, state)) {
			OAuth20Service oauthService = new ServiceBuilder()
					.apiKey(CLIENT_ID)
					.apiSecret(CLIENT_SECRET)
					.callback(REDIRECT_URL)
					.state(state)
					.build(NaverLoginApi.instance());
			OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
			return accessToken;
		}
		return null;
	}

	private String getSession(HttpSession session) {
		// TODO Auto-generated method stub
		return (String) session.getAttribute(SESSION_STATE);
	}

	public String getUserProfile(OAuth2AccessToken oauthToken) throws IOException {
		// TODO Auto-generated method stub
		OAuth20Service oauthService = new ServiceBuilder()
				.apiKey(CLIENT_ID)
				.apiSecret(CLIENT_SECRET)
				.callback(REDIRECT_URL)
				.build(NaverLoginApi.instance());
		OAuthRequest request = new OAuthRequest(Verb.GET,PROFILE_API_URL,oauthService);
		oauthService.signRequest(oauthToken, request);
		Response response = request.send();
		return response.getBody();
	}
}
