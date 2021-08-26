package repository;

import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

//    @Test
//    public void create(){
//        Member member = new Member();
//
//        member.setEmail("lien@maver.com");
//        member.setNickname("룰루랄라");
//        member.setPassword("1234qwer!");
//
//        Member newMember = memberRepository.save(member);
//
//    }

//    @Test
//    public void read(){
//        Optional<Member> member = memberRepository.findByNo(3L);
//        member.ifPresent(selectMember -> {
//            System.out.println(selectMember.getEmail());
//            System.out.println(selectMember.getNickname());
//        });
//    }
}
