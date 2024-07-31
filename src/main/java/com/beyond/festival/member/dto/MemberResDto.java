package com.beyond.festival.member.dto;

import com.beyond.festival.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
