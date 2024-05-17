//package com.example.mdmggreal.oauth;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//
//import java.util.Map;
//
//@Getter
//@Builder
//@RequiredArgsConstructor
//public class OAuthAttributes {
//    private final Map<String, Object> attributes;
//    private final String nameAttributeKey;
//    private final String nickname;
//    private final String email;
//    private final String picture;
//    private final String mobile;
//    private final String age;
//    private final String gender;
//    private final String token;
//
//
//
//    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
//        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
//
//        return OAuthAttributes.builder()
//                .nickname((String) response.get("nickname"))
//                .age((String) response.get("age"))
//                .gender((String) response.get("gender"))
//                .email((String) response.get("email"))
//                .picture((String) response.get("profile_image"))
//                .mobile((String) response.get("mobile"))
//                .token((String) response.get("id") )
//                .attributes(attributes)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//    }
//}