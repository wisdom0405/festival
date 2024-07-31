package com.beyond.festival.member.service;

import com.beyond.festival.common.configs.SecurityConfigs;
import com.beyond.festival.member.domain.Member;
import com.beyond.festival.member.dto.MemberLoginDto;
import com.beyond.festival.member.dto.MemberResDto;
import com.beyond.festival.member.dto.MemberSaveReqDto;
import com.beyond.festival.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class MemberService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public Member memberCreate(MemberSaveReqDto dto){
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("존재하는 이메일입니다.");
        }
        Member member = memberRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        return member;
    }

    public Page<MemberResDto> memberList(Pageable pageable){
        Page<Member> members = memberRepository.findAll(pageable);
        Page<MemberResDto> memberResDtoPage = members.map(a->a.FromEntity());
        return memberResDtoPage;
    }

    public Member login(MemberLoginDto dto){
        Member member = memberRepository.findByEmailOrThrow(dto.getEmail());
        if(!passwordEncoder.matches(dto.getPassword(),member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    public MemberResDto myInfo(){
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailOrThrow(memberEmail);
        return member.FromEntity();
    }
}
