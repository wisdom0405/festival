package com.beyond.festival.member.repository;

import com.beyond.festival.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.parser.Entity;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    default Member findByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new EntityNotFoundException("이메일에 해당하는 회원이 없습니다."));
    }

}
