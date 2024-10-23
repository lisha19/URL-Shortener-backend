package com.example.urlshortner.controller;

import com.example.urlshortner.dto.UserRequestDTO;
import com.example.urlshortner.model.User;
import com.example.urlshortner.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {UrlControllerTest.class})
public class UserControllerTest {

    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void addUserTest() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("name");
        userRequestDTO.setEmail("name@email.com");
        userRequestDTO.setPassword("password");

        User user = new User();
        user.setEmail(userRequestDTO.getEmail());

        when(userService.addUser(any(UserRequestDTO.class))).thenReturn(user);

        // Convert the UserRequestDTO to JSON
        String jsonRequest = new ObjectMapper().writeValueAsString(userRequestDTO);

        // Build the request
        RequestBuilder requestBuilder = post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest); // Set the JSON content

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User created with username: " + user.getEmail()));
    }

}
