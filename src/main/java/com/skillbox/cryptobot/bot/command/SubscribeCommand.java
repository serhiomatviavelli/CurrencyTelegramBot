package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.SubscriberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды подписки на курс валюты
 */
@AllArgsConstructor
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private SubscriberService subscriberService;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String text;
        String regex = "\\d+.\\d{1,2}";
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        if (arguments.length == 0) {
            text = "Сумма подписки не указана";
        } else if (arguments.length == 1 && arguments[0].matches(regex)) {
            double price = Double.parseDouble(arguments[0]);
            text = "Новая подписка создана на стоимость " + price;
            subscriberService.setSubscribe(message.getFrom().getUserName(), price);
        } else {
            text = "Введена неверная форма запроса";
        }

        answer.setText(text);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }
    }
}