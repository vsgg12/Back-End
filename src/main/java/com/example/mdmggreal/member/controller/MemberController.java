package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberService;
import com.example.mdmggreal.member.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final HttpSession httpSession;


    @GetMapping("/")
    public String index(Model model) {
        Member member = (Member) httpSession.getAttribute("member");
        if (member != null) {
            model.addAttribute("member", member.getMemberId());
        }
        return "login";
    }

    /*
     * 네이버 로그인 연결
     */
    @GetMapping("/naver")
    public String login(Model model) {
        model.addAttribute("naverUrl", memberService.getNaverLogin());

        return "naverTest";
    }



    /*
     * 네이버 로그인
     */
    @GetMapping("/signin")
    public String signin () {
        // ID가 없다면 회원가입 화면으로
        return memberService.getNaverLogin();

    }

    /*
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberDTO memberDTO) {
        // 프론트에서 정보 받아와서 save
//        try {
//            memberService.signup(memberDTO);
//            return ResponseEntity.ok("회원가입이 완료되었습니다.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("회원가입에 실패했습니다. " + e.getMessage());
//        }
        return null;
    }

    /*
     * 닉네임 중복 체크
     */


    /*
     * 네이버 로그인 정보 콜백
     */
    @GetMapping("/callback")
    public ResponseEntity<?> callback(HttpServletRequest request) throws Exception {
        MemberDTO member = memberService.getNaverInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body(member);


    }

}
