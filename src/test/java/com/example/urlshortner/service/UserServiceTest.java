package com.example.urlshortner.service;

import com.example.urlshortner.configuration.PasswordEncoderConfig;
import com.example.urlshortner.dto.UserRequestDTO;
import com.example.urlshortner.exception.UserAlreadyRegisteredException;
import com.example.urlshortner.model.User;
import com.example.urlshortner.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PasswordEncoderConfig passwordEncoderConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userService,"userAuthority","USER");
    }

    @Test
    public void addUserTest_WhenNewUser_NoException() throws UserAlreadyRegisteredException {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("name");
        userRequestDTO.setEmail("name@email.com");
        userRequestDTO.setPassword("password");

        when(userRepository.findByEmail(any())).thenReturn(null);
        when(userRepository.save(any())).thenAnswer(invocation  -> invocation.getArgument(0));
        when(passwordEncoderConfig.getEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        User result = userService.addUser(userRequestDTO);

        assertEquals(userRequestDTO.getEmail(),result.getEmail());
        assertEquals(userRequestDTO.getName(),result.getName());
        assertEquals("encodedPassword",result.getPassword());
    }

    @Test(expected = UserAlreadyRegisteredException.class)
    public void addUserTest_WhenUserAlreadyRegistered_ThrowsException() throws UserAlreadyRegisteredException {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        User user = new User();

        when(userRepository.findByEmail(any())).thenReturn(user);

        User result = userService.addUser(userRequestDTO);
    }

}
