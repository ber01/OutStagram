package com.outstagram.boot.member;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MemberRepository extends ReactiveMongoRepository<Member, String> {

    Mono<Member> findByEmail(String email);

}
