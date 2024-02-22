package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.model.entity.Subscriber;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.service.SubscriberService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Рассылка информации о курсе.
 */
@Service
@Slf4j
public class SendInfoCommand {

    @Value("${telegram.bot.notify.delay.value}")
    int infoSendingDelay;

    private final CryptoCurrencyService currencyService;
    private final SubscriberService subscriberService;
    private final CryptoBot cryptoBot;

    public SendInfoCommand(CryptoCurrencyService currencyService, SubscriberService subscriberService, CryptoBot cryptoBot) {
        this.currencyService = currencyService;
        this.subscriberService = subscriberService;
        this.cryptoBot = cryptoBot;
    }

    @Scheduled(fixedDelayString = "PT2M")
    public void sendMessages() {
        List<SendMessage> messages = new ArrayList<>();
        Map<Subscriber, String> subscribers = getSubscribersWithMessageText();

        subscribers.forEach((key, text) -> {
            LocalDateTime lastNotificationTime = key.getNotificationTime();
            if (lastNotificationTime == null ||
                    Duration.between(lastNotificationTime, OffsetDateTime.now()).toMinutes() >= infoSendingDelay) {
                String chatId = key.getChatId().toString();
                messages.add(new SendMessage(chatId, text));
            }
        });

        for (SendMessage message : messages) {
            try {
                subscriberService.setNotificationTime(Long.valueOf(message.getChatId()));
                cryptoBot.execute(message);
            } catch (TelegramApiException e) {
                log.error("Error occurred in send message method: ", e);
            }
        }
    }

    public Map<Subscriber, String> getSubscribersWithMessageText() {
        Double price = null;
        String text = "";
        Map<Subscriber, String> result = new HashMap<>();
        try {
            price = currencyService.getBitcoinPrice();
            text = "Пора покупать, стоимость биткоина: " + TextUtil.toString(price);
            log.info("Last checking: " + LocalDateTime.now() + ", price = " + price);
        } catch (IOException e) {
            log.error("Can't get the price. Error: ", e);
        }
        List<Subscriber> subscribers = subscriberService.getSubscribersByPrice(price);
        for (Subscriber subscriber : subscribers) {
            result.put(subscriber, text);
        }
        return result;
    }
}
