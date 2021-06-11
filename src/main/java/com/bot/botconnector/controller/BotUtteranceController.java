package com.bot.botconnector.controller;

import com.bot.botconnector.domain.ChatMessage;
import com.bot.botconnector.domain.ChatResponse;
import com.bot.botconnector.domain.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BotUtteranceController {
    @PostMapping("/postUtterance")
    public ChatResponse message(@RequestBody ChatMessage chatMessage) {

        if("hi".equalsIgnoreCase(chatMessage.getInputMessage().getText())){
            return getFirstReply();
        }
        if("what is your name".equalsIgnoreCase(chatMessage.getInputMessage().getText())){
            return getSecondReply();
        }
        if("what created you".equalsIgnoreCase(chatMessage.getInputMessage().getText())){
            return getThirdReply();
        }
        return getFirstReply();
    }

    private ChatResponse getFirstReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Hi").build())).build();
    }
    private ChatResponse getSecondReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Jarvis").build())).build();
    }
    private ChatResponse getThirdReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Tony Stark").build())).build();
    }
}
