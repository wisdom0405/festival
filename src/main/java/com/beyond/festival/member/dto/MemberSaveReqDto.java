package com.beyond.festival.member.dto;

import com.beyond.festival.member.domain.Member;
import com.beyond.festival.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSaveReqDto {

    private String name;

    // 데이터 바인딩할 때 에러 터뜨리기 위한 용도
    @NotEmpty(message = "email은 필수 값 입니다.")
    private String email;

    @NotEmpty(message = "password는 필수 값 입니다.")
    @Size(min=4, message = "password는 최소 4자 이상입니다.")
    private String password;

    private Role role = Role.USER;

    public Member toEntity(String password){
       return Member.builder()
                .name(this.name)
                .email(this.email)
                .password(password)
                .role(this.role)
                .build();
    }
}
