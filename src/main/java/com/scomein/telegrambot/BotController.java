package com.scomein.telegrambot;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@EnableScheduling
public class BotController {

    @Autowired
    private UpdateHandler handler;

    @Autowired
    private TelegramBot bot;

    @PostConstruct
    public void init() {
//        bot.setUpdatesListener(list -> {
//            for(int i = 0; i < list.size(); i++) {
//                handler.handleUpdate(list.get(i));
//            }
//            return list.get(list.size()).updateId();
//        });

    }

    private static int offset = -1;

    @Scheduled(fixedDelay = 1000)
    @RequestMapping("/poll")
    public void poll() {
        try {
            GetUpdates getUpdates = new GetUpdates().limit(100).offset(offset).timeout(1).timeout(0);
            GetUpdatesResponse getUpdatesResponse = bot.execute(getUpdates);
            List<Update> updates = getUpdatesResponse.updates();
            if(!updates.isEmpty()) {
                offset = updates.get(updates.size() - 1).updateId() + 1;
            }
            updates.forEach(this::webhook);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public void webhook(@RequestBody Update update) {
        handler.handleUpdate(update);
    }
}
