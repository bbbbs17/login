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
import org.springframework.web.bind.annotation.PostMapping;


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

    @GetMapping("/member/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail"); //강제 형변환
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";

    }
    @PostMapping("/member/updat")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "redirect:/member/";
    }
}
