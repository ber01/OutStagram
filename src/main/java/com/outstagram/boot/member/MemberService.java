package com.outstagram.boot.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 더미 데이터 저장 및 테스트
    public Mono<Member> saveMember(Member member) {
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        return this.memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Mono<Member> memberMono = memberRepository.findByEmail(username);
        Member member = memberMono.block();

        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        return new MemberAdapter(member);
    }

    public Mono<Member> save(Member member) {
        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        member.setRoles(Set.of(MemberRole.USER));
        return this.memberRepository.save(member);
}
}
