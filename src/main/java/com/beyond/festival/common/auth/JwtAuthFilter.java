package com.beyond.festival.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JwtAuthFilter extends GenericFilter{

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest)request).getHeader("Authorization");

        try{
            if(bearerToken != null){
                if(!bearerToken.substring(0,7).equals("Bearer ")){
                    throw new AuthenticationException("Bearer 형식이 아닙니다.");
                }

                String token = bearerToken.substring(7); // 7번째 이후 글자만 잘라옴
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody(); // payLoad에 있는 정보(이메일, 권한)

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_"+claims.get("role")));

                UserDetails userDetails = new User(claims.getSubject(),"",authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request,response);
        }catch (Exception e){
            log.error(e.getMessage());
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("token error");
        }
    }
}
