package com.example.mdmggreal.member;

import io.micrometer.common.util.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;

    private final static String NAVER_AUTH_URI = "https://nid.naver.com";
    private final static String NAVER_API_URI = "https://openapi.naver.com";

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*
     * 네이버 로그인 사이트
     */
    public String getNaverLogin() {
        return NAVER_AUTH_URI + "/oauth2.0/authorize"
                + "?client_id=" + NAVER_CLIENT_ID
                + "&redirect_uri=" + NAVER_REDIRECT_URI
                + "&response_type=code";
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
            params.add("redirect_url", NAVER_REDIRECT_URI);

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
        String profileImage = String.valueOf(account.get("profileImage"));

        return MemberDTO.builder()
                .memberId(id)
                .email(email)
                .nickname(nickname)
                .mobile(mobileNumber)
                .profileImage(profileImage)
                .role(MemberRole.USER)
                .build();
    }

    /*
     * 회원가입
     */
    @Transactional
    public void signup(MemberDTO memberDTO) {

        if(memberRepository.existByMemberId(memberDTO.getMemberId())) {
            return;
        }

        Member member = new Member();
        member.setMemberId(memberDTO.getMemberId());
        member.setEmail(memberDTO.getEmail());
        member.setNickname(memberDTO.getNickname());
        member.setProfileImage(memberDTO.getProfileImage());

        memberRepository.save(member);

    }
}
