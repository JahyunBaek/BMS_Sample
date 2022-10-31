package com.szs.jobis.AppRunner;

import com.szs.jobis.Entity.UserPossibleEntity;
import com.szs.jobis.Repository.initRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitRunner {

    private final initRepository initRepository;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            initRepository.save(UserPossibleEntity.builder().userId("홍길동").regNo("860824-1655068").build());
            initRepository.save(UserPossibleEntity.builder().userId("김둘리").regNo("921108-1582816").build());
            initRepository.save(UserPossibleEntity.builder().userId("마징가").regNo("880601-2455116").build());
            initRepository.save(UserPossibleEntity.builder().userId("베지터").regNo("910411-1656116").build());
            initRepository.save(UserPossibleEntity.builder().userId("손오공").regNo("820326-2715702").build());
        };
    }
}
