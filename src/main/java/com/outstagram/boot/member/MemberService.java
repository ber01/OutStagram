package com.outstagram.boot.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

}
