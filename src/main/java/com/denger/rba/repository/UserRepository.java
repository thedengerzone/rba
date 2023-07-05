package com.denger.rba.repository;


import com.denger.rba.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOib(String oib);
    void deleteByOib(String oib);

}
