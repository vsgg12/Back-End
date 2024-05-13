package com.example.mdmggreal.member.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repo.MemberRepository;
import com.example.mdmggreal.oauth.OAuthAttributes;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    private final HttpSession httpSession;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;

    private final static String NAVER_AUTH_URI = "https://nid.naver.com";
    private final static String NAVER_API_URI = "https://openapi.naver.com";

    public MemberService(MemberRepository memberRepository, HttpSession httpSession) {
        this.memberRepository = memberRepository;
        this.httpSession = httpSession;
    }

    /*
     * 네이버 로그인 정보 콜백
     */
    public MemberDTO getNaverInfo(String code) throws Exception {
        if(StringUtils.isEmpty(code)) throw new Exception("Failed");

        String accessToken = "";
//        String refreshToken = "";

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", NAVER_CLIENT_ID);
            params.add("client_secret", NAVER_CLIENT_SECRET);
            params.add("code", code);
            params.add("redirect_uri", NAVER_REDIRECT_URI);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    NAVER_AUTH_URI + "/oauth2.0/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken = (String) jsonObj.get("access_token");
//            refreshToken = (String) jsonObj.get("refresh_token");
        } catch (Exception e) {
            throw new Exception("API call failed");
        }

        return getUserInfoWithToken(accessToken);
    }

    /*
     * 네이버 로그인 정보 토근을 통해 가져오기
     */
    private MemberDTO getUserInfoWithToken(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                NAVER_API_URI + "/v1/nid/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("response");

        String id = String.valueOf(account.get("id"));
        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(account.get("nickname"));
        String mobileNumber = String.valueOf(account.get("mobile"));
        String profileImage = String.valueOf(account.get("profile_image"));
        String gender = String.valueOf(account.get("gender"));
        String age = String.valueOf(account.get("age"));

        MemberDTO memberDTO =  MemberDTO.builder()
                .token(id)
                .email(email)
                .nickname(nickname)
                .mobile(mobileNumber)
                .profileImage(profileImage)
                .age(age)
                .gender(gender)
                .build();

        if (!memberRepository.existsByToken(memberDTO.getToken())) {
            // 회원이 아닌 경우 프론트엔드로 회원가입 유도
            return memberDTO;
        }

        // 세션에 사용자 정보 저장
        httpSession.setAttribute("memberDTO", memberDTO);

        return memberDTO;
    }

    /*
     * 회원가입
     */
    @Transactional
    public Member signup(MemberDTO memberDTO) {

        if (memberRepository.existsByMobile(memberDTO.getMobile())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
       return memberRepository.save(Member.from(memberDTO));
    }

    /*
     * 토큰 존재 여부
     */
    public boolean isMemberExist (String token) {
        return memberRepository.existsByToken(token);

    }

    /*
     * 닉네임 중복 여부
     */
    public boolean isNicknameAvailable(String nickname) {
        return !memberRepository.existsByNickname(nickname);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.ofNaver(userNameAttributeName, oAuth2User.getAttributes());

    //        Member member = signup(attributes);
    //        httpSession.setAttribute("member", member);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(null)),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

    }

}