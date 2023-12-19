package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.model.entity.Subscriber;
import com.skillbox.cryptobot.model.repository.SubscribersRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Data
@Service
public class SubscriberService {

    private SubscribersRepository repository;

    public void addNewSubscriber(String telegramId) {
        if (repository.findByTelegramId(telegramId).isEmpty()) {
            repository.save(Subscriber.builder()
                    .userId(UUID.randomUUID())
                    .telegramId(telegramId)
                    .price(null)
                    .build());
        }
    }

    public void setSubscribe(String telegramId, double price) {
        Optional<Subscriber> subscriberOptional = repository.findByTelegramId(telegramId);
        if (subscriberOptional.isPresent()) {
            Subscriber subscriber = subscriberOptional.get();
            subscriber.setPrice(price);
            repository.save(subscriber);
        }
    }
}
