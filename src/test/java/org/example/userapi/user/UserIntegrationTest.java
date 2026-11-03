package org.example.userapi.user;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestRestTemplate
class UserIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;


    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void 포트_확인() throws InterruptedException{
        System.out.println("접속가능한 포트:"+ port);
        Thread.sleep(30000);

    }

    @Test
    void 유저를_등록한다() {
        User user = new User("김철수", "kim@example.com");

        ResponseEntity<User> response = restTemplate.postForEntity(
                "/users", user, User.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("김철수", response.getBody().getName());

    }

    @Test
    void 저장된_유저를_ID로_조회한다() {
        User saved = userRepository.save(new User("김철수", "kim@example.com"));

        ResponseEntity<User> response = restTemplate.getForEntity(
                "/users/" + saved.getId(), User.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("김철수", response.getBody().getName());
    }

    @Test
    void 존재하지_않는_유저_조회시404() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/users/999", String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
