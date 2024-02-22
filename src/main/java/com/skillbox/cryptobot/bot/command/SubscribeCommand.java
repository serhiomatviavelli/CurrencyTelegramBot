package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.service.SubscriberService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

/**
 * Обработка команды подписки на курс валюты
 */
@AllArgsConstructor
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private SubscriberService subscriberService;
    private CryptoCurrencyService currencyService;

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
        String telegramId = message.getFrom().getUserName();

        answer.setChatId(message.getChatId());

        if (arguments.length == 0) {
            text = "Сумма подписки не указана";
        } else if (arguments.length == 1 && arguments[0].matches(regex)) {
            double price = Double.parseDouble(arguments[0]);
            double currentPrice = 0;
            try {
                currentPrice = currencyService.getBitcoinPrice();
            } catch (IOException e) {
                log.error("Can't get the price. Error: ", e);
            }
            text = "Новая подписка создана на стоимость " + TextUtil.toString(price);
            if (price > currentPrice) {
                text += "\nПора покупать, стоимость биткоина: " + TextUtil.toString(currentPrice);
                subscriberService.setNotificationTime(message.getChatId());
            }
            subscriberService.setSubscribe(telegramId, price);
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