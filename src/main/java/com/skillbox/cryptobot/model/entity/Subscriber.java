package com.skillbox.cryptobot.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subscribers")
public class Subscriber {

    @Id
    private UUID userId;
    private String telegramId;
    private Double price;
    private LocalDateTime notificationTime;
    private Long chatId;

}
