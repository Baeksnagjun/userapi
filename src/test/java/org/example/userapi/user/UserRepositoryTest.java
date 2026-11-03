package org.example.userapi.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void 유저를저장하고조회한다() {
        User user = new User("홍길동", "hong@example.com");
        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("홍길동", found.get().getName());

    }

    @Test
    void 이메일로_유저조회() {

        userRepository.save(new User("홍길동", "hong@example.com"));
        userRepository.save(new User("박민수", "park@example.com"));
        Optional<User> found = userRepository.findByEmail("hong@example.com");

        assertTrue(found.isPresent());
        assertEquals("홍길동", found.get().getName());


    }
}