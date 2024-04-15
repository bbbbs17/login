package Member.dailylogin.controller;

import Member.dailylogin.dto.MemberDTO;
import Member.dailylogin.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor

public class MemberController {

    //생성자 주입
    private final MemberService memberService;

    //회원가입 페이지 출력
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO)
    //@RequestParam은 html에서의 memberEmail을 받아와서 넘겨줌
    {
        System.out.println("MemberController.save");
        System.out.println("memberEmail = " + memberDTO);
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

    //회원목록
    @GetMapping("/member/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList",memberDTOList);
        return "list";
    }

    //회원수정

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail"); //강제 형변환
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";

    }
 //   @PostMapping("/member/update")
   // public String update(@ModelAttribute MemberDTO memberDTO){
     //   memberService.update(memberDTO);
       // return "redirect:/member/";
   // }
 @PostMapping("/member/update")
 public String update(@ModelAttribute MemberDTO memberDTO, HttpSession session){
     memberService.update(memberDTO);
     // 세션 정보 업데이트
     session.setAttribute("loginEmail", memberDTO.getMemberEmail());
     return "redirect:/";
 }


    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model){
        MemberDTO memberDTO=memberService.findById(id);
        model.addAttribute("member",memberDTO);
        return "detail";
    }

    //회원정보 삭제

    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }


    //탈퇴하기 기능





}
