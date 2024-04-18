package Member.dailylogin.controller;

import Member.dailylogin.dto.MemberDTO;
import Member.dailylogin.entity.MemberEntity;
import Member.dailylogin.repository.MemberRepository;
import Member.dailylogin.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor

public class MemberController {

    //생성자 주입
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    //회원가입 페이지 출력
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO)
    //@RequestParam은 html에서의 memberEmail을 받아와서 넘겨줌
    {
        memberService.save(memberDTO);

        return "index";
    }


    //로그인버튼 눌러서 들어가서 로그인할때 사용
    //@GetMapping("/member/login")
    //public String loginForm() {
    //  return "login";
    //}


    //로그인

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            //login성공
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            //login실패
            return "index";
        }
    }

    @GetMapping("/member/home")
    public String home(HttpSession session) {
        String loginEmail = (String) session.getAttribute("loginEmail");
        if (loginEmail != null) {
            // 세션에 로그인 정보가 있는 경우, 로그인된 상태로 다시 이동
            return "redirect:/member/login";
        } else {
            // 세션에 로그인 정보가 없는 경우, 로그인 페이지로 이동
            return "redirect:/index";
        }
    }

    //회원목록  현재 지금은  html 에서 hidden처리--사용하지않음
    @GetMapping("/member/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    //회원수정

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {//Controller 생성 view로 보내는역할
        String myEmail = (String) session.getAttribute("loginEmail"); //강제 형변환
        //현재의 세션의 loginEmail을 myEmail저장
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        //updateMember를 키로 html로보냄 안에는 memberDTO 정보가있음
        return "update";

    }

    //   @PostMapping("/member/update")
    // public String update(@ModelAttribute MemberDTO memberDTO){
    //   memberService.update(memberDTO);
    // return "redirect:/member/";
    // }
    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        memberService.update(memberDTO);
        // 세션 정보 업데이트
        session.setAttribute("loginEmail", memberDTO.getMemberEmail());
        return "redirect:/";
    }


    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    //회원정보 삭제

    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    //로그아웃
    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/member/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        if (checkResult != null) {
            return "ok";
        } else {
            return "no";
        }

    }


    //탈퇴하기 기능


}
