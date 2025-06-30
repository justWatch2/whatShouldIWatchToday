package avengers.waffle.service;


import avengers.waffle.repository.userRepository.MemberRepositoryho;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor

public class MemberRepositoryhoImpl {

    private final MemberRepositoryho memberRepository;
}
