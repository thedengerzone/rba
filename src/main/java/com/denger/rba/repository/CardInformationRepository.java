package com.denger.rba.repository;

import com.denger.rba.entity.card.CardInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardInformationRepository extends JpaRepository<CardInformation, Long> {

    Optional<CardInformation> findCardInformationByOib(String oib);

}
