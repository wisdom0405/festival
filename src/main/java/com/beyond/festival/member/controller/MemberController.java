package com.beyond.festival.member.controller;

import com.beyond.festival.common.auth.JwtTokenProvider;
import com.beyond.festival.common.dto.CommonErrorDto;
import com.beyond.festival.common.dto.CommonResDto;
import com.beyond.festival.member.domain.Member;
import com.beyond.festival.member.dto.MemberLoginDto;
import com.beyond.festival.member.dto.MemberRefreshDto;
import com.beyond.festival.member.dto.MemberResDto;
import com.beyond.festival.member.dto.MemberSaveReqDto;
import com.beyond.festival.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/festival")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Qualifier("1")
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    @Autowired
    public MemberController(MemberService memberService,
                            JwtTokenProvider jwtTokenProvider,
                            @Qualifier("1")RedisTemplate<String, Object> redisTemplate){
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/member/create")
    public ResponseEntity<?> memberCreate(@Valid @RequestBody MemberSaveReqDto dto){
        Member member = memberService.memberCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "회원가입 성공", member.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED); // header에 들어가는 상태
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/list")
    public ResponseEntity<Object> memberList(Pageable pageable){
        Page<MemberResDto> memberResDtos = memberService.memberList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "memberList 조회 성공", memberResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/member/myinfo")
    public ResponseEntity<Object> myInfo(){
        MemberResDto memberResDto = memberService.myInfo();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "myInfo 조회성공", memberResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginDto dto){
        Member member = memberService.login(dto);

        String jwtToken = jwtTokenProvider.createToken(member.getEmail(),member.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(), member.getRole().toString());

        redisTemplate.opsForValue().set(member.getEmail(), refreshToken, 240, TimeUnit.HOURS);

        Map<String, Object> logInfo = new HashMap<>();
        logInfo.put("id", member.getId());
        logInfo.put("token", jwtToken);
        logInfo.put("refresh token", refreshToken);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "로그인 성공", logInfo);

        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> generateNewAccessToken(@RequestBody MemberRefreshDto dto){
        String rt = dto.getRefreshToken();
        Claims claims = null;
        try{
            // 코드를 통해 rt검증
            claims = Jwts.parser().setSigningKey(secretKeyRt).parseClaimsJws(rt).getBody();
        }catch (Exception e){
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.UNAUTHORIZED.value(),"invalid refresh token"), HttpStatus.UNAUTHORIZED)
        }
        String email = claims.getSubject();
        String role = claims.get("role").toString();

        Object obj = redisTemplate.opsForValue().get(email);
        if(obj == null || !obj.toString().equals(email)){
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.UNAUTHORIZED.value(),"invalid refresh token"),HttpStatus.UNAUTHORIZED);
        }
        // 새로운 access token 받아냄
        String newAt = jwtTokenProvider.createToken(email, role);
        Map<String, Object> info = new HashMap<>();
        info.put("token", newAt);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "at is renewed", info);

        return new ResponseEntity<>(commonResDto ,HttpStatus.OK);
    }

}
