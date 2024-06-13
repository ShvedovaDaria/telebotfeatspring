package org.example;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class MyTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;

    public MyTelegramBot(DefaultBotOptions options, String botUsername, String botToken) {
        super(options);
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            String responseText = processCommand(messageText);
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(responseText);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Error sending message: ", e);
            }
        }
    }

    private String processCommand(String messageText) {
        switch (messageText.toLowerCase()) {
            case "/start":
                return "Welcome! Send /fact to get an interesting fact.";
            case "/fact":
                return getRandomFact();
            default:
                return "Unknown command. Try /start or /fact.";
        }
    }

    private String getRandomFact() {
        String[] facts = {
                "Honey never spoils.",
                "Bananas are berries, but strawberries aren't.",
                "Humans share 50% of their DNA with bananas.",
                "A day on Venus is longer than a year on Venus."
        };
        int index = (int) (Math.random() * facts.length);
        return facts[index];
    }
}
