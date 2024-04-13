package Member.dailylogin.service;

import Member.dailylogin.dto.MemberDTO;
import Member.dailylogin.entity.MemberEntity;
import Member.dailylogin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO) {
        //1.dto -> entity 변환

        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
        //2. repository의 save 메서드 호출
    }

    public MemberDTO login(MemberDTO memberDTO) {
        //1.회원이 입력한 이메일로 db에서 조회를함
        //2.db에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(byMemberEmail.isPresent()) {
            //조회 결과가 있다==해당 이메일을 가진 회원정보가 있음
            MemberEntity memberEntity = byMemberEmail.get(); //객체를 가져올수있음
            if (memberEntity.getMeberPassword().equals(memberDTO.getMemberPassword())) {
                //비밀번호일치
                //entity 객체를 dto 변환후 리턴 entity객체를 그대로 쓰는건 db위험 증가
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;

            }else {
                return null;
            }
        }else {
            //조회 결과가 없다==해당 이메일을 가진 회원정보가 없음
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);

        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());

        }else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toupdateMemberEntity(memberDTO));
    }
}