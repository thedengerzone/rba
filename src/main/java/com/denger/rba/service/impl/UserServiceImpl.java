package com.denger.rba.service.impl;


import com.denger.rba.entity.card.CardInformation;
import com.denger.rba.entity.user.User;
import com.denger.rba.repository.CardInformationRepository;
import com.denger.rba.repository.UserRepository;
import com.denger.rba.service.UserService;
import com.denger.rba.utils.FileUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"User"})
public class UserServiceImpl implements UserService {

    @Value("${cards.directory.path}")
    String directoryPath;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    private final CardInformationRepository cardInformationRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,CardInformationRepository cardInformationRepository) {
        this.userRepository = userRepository;
        this.cardInformationRepository = cardInformationRepository;
    }

    @Override
    public File generate(String oib) {
        Optional<User> user = fetchUser(oib);
        if (user.isPresent()) {
            try {
                logger.info("User is found with oib: {}", oib);
                return FileUtils.generateFileFromUserInformation(user.get(),directoryPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("User not found");
        return null;

    }

    @Cacheable(key = "#oib")
    public Optional<User> fetchUser(String oib) {
        return userRepository.findByOib(oib);
    }

    @Override
    @Transactional
    public User createUser(User userRequest) {
        Optional<CardInformation> optionalCardInformation = cardInformationRepository.findCardInformationByOib(userRequest.getOib());
        CardInformation cardInformation;
        if (optionalCardInformation.isPresent()) {
            cardInformation = optionalCardInformation.get();
            cardInformation.setLastModified(LocalDateTime.now());
        } else {
            cardInformation = new CardInformation();
            cardInformation.setOib(userRequest.getOib());
        }

        User user = User.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .oib(userRequest.getOib())
                .cardInformation(cardInformation)
                .build();

        cardInformation.setUser(user);
        cardInformationRepository.save(cardInformation);
        return userRepository.save(user);
    }


    @Override
    @Transactional
    public void deleteUser(String oib) {
        User user = userRepository.findByOib(oib).orElseThrow();

        CardInformation cardInformation = user.getCardInformation();
        if (cardInformation != null) {
            cardInformation.setUser(null);
            cardInformation.setStatus(false);
            cardInformationRepository.save(cardInformation);
        }

        userRepository.delete(user);
        FileUtils.setFileToInactive(user,directoryPath);
    }
}
