package com.skillbox.cryptobot.model.repository;

import com.skillbox.cryptobot.model.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscribersRepository extends JpaRepository<Subscriber, UUID> {

    Optional<Subscriber> findByTelegramId(String telegramId);

    List<Subscriber> findAllByPriceGreaterThanEqual(Double price);

    Optional<Subscriber> findByChatId(Long chatId);
}
