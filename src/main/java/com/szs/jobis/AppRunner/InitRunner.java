package com.szs.jobis.AppRunner;

import com.szs.jobis.Entity.UserPossibleEntity;
import com.szs.jobis.Repository.initRepository;
import com.szs.jobis.util.AES256;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitRunner {

    private final initRepository initRepository;

    //private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            initRepository.save(UserPossibleEntity.builder().name("홍길동").regNo(AES256.encryptAES256("860824-1655068")).build());
            initRepository.save(UserPossibleEntity.builder().name("김둘리").regNo(AES256.encryptAES256("921108-1582816")).build());
            initRepository.save(UserPossibleEntity.builder().name("마징가").regNo(AES256.encryptAES256("880601-2455116")).build());
            initRepository.save(UserPossibleEntity.builder().name("베지터").regNo(AES256.encryptAES256("910411-1656116")).build());
            initRepository.save(UserPossibleEntity.builder().name("손오공").regNo(AES256.encryptAES256("820326-2715702")).build());
        };
    }
}
