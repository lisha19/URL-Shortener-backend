package com.example.urlshortner.service;

import com.example.urlshortner.configuration.PasswordEncoderConfig;
import com.example.urlshortner.dto.UserRequestDTO;
import com.example.urlshortner.exception.UserAlreadyRegisteredException;
import com.example.urlshortner.model.User;
import com.example.urlshortner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Value("${user.authority}")
    private String userAuthority;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderConfig passwordEncoderConfig;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public User addUser(UserRequestDTO userRequestDTO) throws UserAlreadyRegisteredException {

        User userFromDB = userRepository.findByEmail(userRequestDTO.getEmail());
        if(userFromDB != null){
            throw new UserAlreadyRegisteredException("This email is already registered");
        }

        User user = userRequestDTO.toUser();
        user.setAuthorities(userAuthority);
        user.setPassword(passwordEncoderConfig.getEncoder().encode(userRequestDTO.getPassword()));

        return userRepository.save(user);
    }
}
