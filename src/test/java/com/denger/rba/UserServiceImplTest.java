package com.denger.rba;

import com.denger.rba.entity.card.CardInformation;
import com.denger.rba.entity.user.User;
import com.denger.rba.repository.CardInformationRepository;
import com.denger.rba.repository.UserRepository;
import com.denger.rba.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardInformationRepository cardInformationRepository;


    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository,cardInformationRepository);
        user = User.builder()
                .name("Denis")
                .surname("Gerić")
                .oib("15088667719")
                .cardInformation(new CardInformation())
                .build();
    }

    @Test
    void UserExistFileGenerated() {

        when(userRepository.findByOib(user.getOib())).thenReturn(Optional.of(user));

        File generatedFile = userService.generate(user.getOib());

        assertNotNull(generatedFile);
    }

    @Test
    void UserNotExistFileGenerated() {
        when(userRepository.findByOib(user.getOib())).thenReturn(Optional.empty());

        File generatedFile = userService.generate(user.getOib());

        assertNull(generatedFile);
    }

    @Test
    void UserSaved() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user, createdUser);
    }

    @Test
    void UserSaveFail() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenThrow(new IllegalArgumentException("Save failed"));

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }



}
