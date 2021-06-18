package com.bot.botconnector.controller;

import com.bot.botconnector.domain.BotState;
import com.bot.botconnector.domain.ChatMessage;
import com.bot.botconnector.domain.ChatResponse;
import com.bot.botconnector.domain.Content;
import com.bot.botconnector.domain.Message;
import com.bot.botconnector.domain.QuickReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class BotUtteranceController {
    private static final String SUCCESS = "success";
    private static final String FALL_BACK = "fallback";
    @PostMapping(path = "/postUtterance")
    public ChatResponse message(@RequestPart(value = "file", required = false) ChatMessage chatMessage) {
        if(chatMessage == null) {
            log.info("chat message null:");
            return getFirstReply();
        }
        log.info("Post utterance with genesys-conversation-id:{},bot-session-id:{} :",chatMessage.getGenesysConversationId(),chatMessage.getBotSessionId());
        if("hi".equalsIgnoreCase(chatMessage.getInputMessage().getText())){
            return getFirstReply();
        }
        if("what is your name".equalsIgnoreCase(chatMessage.getInputMessage().getText())){
            return getSecondReply();
        }
        if("what created you".equalsIgnoreCase(chatMessage.getInputMessage().getText())){
            return getThirdReply();
        }
        return getFailedReply();
    }

    private ChatResponse getFirstReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Hi,I am virtual assistant")
                        .content(List.of(Content.builder()
                                .contentType("Structured")
                                .quickReply(QuickReply.builder()
                                        .text("<div onclick=\\\"window.inqFrame.Application.sendVALinkClicked(event);\\\">Hi,I am virtual assistant</div>")
                                        .build()).build()))
                        .build()))
                .intent(SUCCESS)
                .parameters(Map.of("htmlResponse","<div onclick=\\\"window.inqFrame.Application.sendVALinkClicked(event);\\\">Hi,I am virtual assistant</div>"))
                .additionalProperties(true)
                .botState(BotState.COMPLETE).build();
    }
    private ChatResponse getSecondReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Jarvis").build()))
                .intent(SUCCESS)
                .botState(BotState.COMPLETE).build();
    }
    private ChatResponse getThirdReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Tony Stark").build()))
                .intent(SUCCESS)
                .botState(BotState.COMPLETE).build();
    }
    private ChatResponse getFailedReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Please wait. I am looking for a colleague who is available to chat.").build()))
                .intent(FALL_BACK)
                .botState(BotState.FAILED).build();
    }
}
