package org.example.userapi.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void id로_유저_조회() throws Exception{
        User user = new User("홍길동", "hong@example.com");

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.email").value("hong@example.com"));
    }

    @Test
    void 유저생성() throws Exception{
        User input = new User("이형희", "lee@example.com");
        when(userService.register(any(User.class))).thenReturn(input);


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("이형희"));

    }

    @Test
    void 존재하지_않는_유저_조회_시_404() throws Exception {
        when(userService.findById(999L)).thenThrow(new IllegalArgumentException("존재하지않는 유저입니다."));
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());

    }
}