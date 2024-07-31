package com.beyond.festival.member.domain;

import com.beyond.festival.common.dto.BaseTimeEntity;
import com.beyond.festival.member.dto.MemberResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id; // 회원 id

    @Column(length=20, nullable = false)
    private String name; // 이름

    @Column(unique = true, nullable = false)
    private String email; //이메일 (unique)

    @Column(nullable = false)
    private String password; // 패스워드

    @Enumerated(EnumType.STRING)
    private Role role;

    public MemberResDto FromEntity(){
        return MemberResDto.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .role(this.role)
                .build();
    }
}
