package com.skillbox.cryptobot.model.repository;

import com.skillbox.cryptobot.model.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscribersRepository extends JpaRepository<Subscriber, UUID> {

    Optional<Subscriber> findByTelegramId(String telegramId);

}
