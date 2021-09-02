package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.AuthType;
import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.MemberType;
import com.megait.nocoronazone.form.SettingForm;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

//    @PostConstruct
    @Profile("local")
    public void createNewMember(){

        Member member = Member.builder()
                .email("admin@test.com")
                .password(passwordEncoder.encode("qwe123"))
                .memberType(MemberType.ROLE_ADMIN)
                .nickname("하하")
                .authType(AuthType.GENERAL)
                .certification(true)
                .build();

        memberRepository.save(member);

    }

    public Member processNewUser(SignUpForm signUpForm){

        Member member = Member.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .memberType(MemberType.ROLE_USER)
                .authType(AuthType.GENERAL)
                .build();

        Member newMember = memberRepository.save(member);

        emailService.sendEmail(newMember);

        return newMember;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optional = memberRepository.findByEmail(username);
        if (optional.isEmpty()){
            throw new UsernameNotFoundException(username);
        }

        return new MemberUser(optional.get());
    }


    public void checkNickname(String nickname) {

        Optional<Member> member = memberRepository.findByNickname(nickname);
        if(member.isEmpty()){
            throw new IllegalArgumentException("available nickname");
        }
    }

    @Transactional
    public void checkEmailToken(String token, String email) {

        Optional<Member> opt = memberRepository.findByEmail(email);

        if (opt.isEmpty()){
            throw new IllegalArgumentException("wrong email");
        }

        Member member = opt.get();
        if(!member.isValidToken(token)){
            throw new IllegalArgumentException("wrong token");
        }

        member.completeSignup();
    }

    public void login(Member member) {
        MemberUser memberUser = new MemberUser(member);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        memberUser,
                        memberUser.getMember().getPassword(),
                        memberUser.getAuthorities()
                );

        SecurityContext ctx = SecurityContextHolder.getContext();
        ctx.setAuthentication(token);
    }


    @Transactional
    public Member updateMember(Long no, SettingForm settingForm) {
        Member member = memberRepository.findByNo(no).get();
        member.update(settingForm);

        return member;
    }


    public Object getMember(Member member) {
        return memberRepository.getById(member.getNo());
    }


    public Member getNicknameMember(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);

        if(optionalMember.isEmpty()){
            throw new IllegalArgumentException("wrong nickname");
        }

        return optionalMember.get();
    }
}



