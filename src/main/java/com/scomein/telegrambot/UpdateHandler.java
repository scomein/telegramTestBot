package com.scomein.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import com.pengrad.telegrambot.request.SendMessage;
import com.scomein.telegrambot.dao.UserDao;
import com.scomein.telegrambot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UpdateHandler {

    @Autowired
    UserDao userDao;

    @Autowired
    TelegramBot telegramBot;

    public static final String HELLO_TEXT = "Добро Пожаловать!";
    public static final String CLEANED_MESSAGES = "Ваши сообщения удалены";


    public void handleUpdate(Update update) {
        Message message = update.message();

        User currentUser = userDao.getById(message.chat().id());
        if(currentUser == null) {
            User user = new User(message.chat().id(), message.chat().firstName() + message.chat().lastName());
            userDao.save(user);
        }

        String text = message.text();
        String[] splittedText = text.split(" ");
        Long chatId = message.chat().id();

        switch (splittedText[0]) {
            case "/start":start(chatId);
                break;
            case "/my":my(chatId, currentUser);
                break;
            case "/clean":clean(chatId, currentUser);
                break;
            default:saveMessage(text, currentUser);
        }
    }

    private void start(long chatId) {
        sendMessage(HELLO_TEXT, chatId);
    }

    private void my(long chatId, User user) {
        StringBuilder messageBuilder = new StringBuilder();
        for(com.scomein.telegrambot.entities.Message message : user.getMessages()) {
            messageBuilder.append(message.getText()).append("\n");
        }
        sendMessage(messageBuilder.toString(), chatId);
    }

    private void saveMessage(String message, User user) {
        com.scomein.telegrambot.entities.Message m = new com.scomein.telegrambot.entities.Message();
        m.setCreateDate(new Date());
        m.setText(message);
        m.setUser(user);

        user.getMessages().add(m);
        userDao.save(user);
    }

    private void clean(long chatId, User user) {
        user.getMessages().clear();
        userDao.save(user);
        sendMessage(CLEANED_MESSAGES, chatId);
    }

    private void sendMessage(String text, long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        telegramBot.execute(sendMessage);

    }
}
