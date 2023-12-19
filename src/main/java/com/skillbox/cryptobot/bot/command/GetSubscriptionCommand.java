package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.model.entity.Subscriber;
import com.skillbox.cryptobot.model.repository.SubscribersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private SubscribersRepository repository;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        String text = "";

        Optional<Subscriber> subscriber = repository.findByTelegramId(message.getFrom().getUserName());
        if (subscriber.isPresent()) {
            Double price = subscriber.get().getPrice();
            text = price == null ? "Активные подписки отсутствуют"
                    : "Вы подписаны на стоимость биткоина " + price + " USD";
        }

        answer.setText(text);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /get_subscription command", e);
        }
    }
}