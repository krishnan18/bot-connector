package com.bot.botconnector.controller;

import com.bot.botconnector.domain.BotState;
import com.bot.botconnector.domain.ChatMessage;
import com.bot.botconnector.domain.ChatResponse;
import com.bot.botconnector.domain.Content;
import com.bot.botconnector.domain.Count;
import com.bot.botconnector.domain.Message;
import com.bot.botconnector.domain.QuickReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private Count count;
    @PostMapping(path = "/postUtterance")
    public ChatResponse message(@RequestPart(value = "file", required = false) ChatMessage chatMessage) {
        if(count.getCount() > 2) {
            count.setCount(0);
        }
        count.setCount(count.getCount()+1);
        if(chatMessage == null) {
            log.info("chat message null:");
            if(count.getCount() <= 1){
                log.info("First reply");
                return getFirstReply();
            }
            if(count.getCount() == 2){
                log.info("Second reply");
                return getSecondContentReply();
            }
            if(count.getCount() > 2){
                log.info("Second reply");
                return getEscalationReply();
            }
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
                        .text("<div onclick=\\\"window.inqFrame.Application.sendVALinkClicked(event);\\\">Hi,I am virtual assistant</div>")
                        .build()))
                .intent(SUCCESS)
                .parameters(Map.of("htmlResponse","<div onclick=\\\"window.inqFrame.Application.sendVALinkClicked(event);\\\">Hi,I am virtual assistant</div>"))
                .additionalProperties(true)
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getSecondContentReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Structured")
                        .text("Would you like to talk to an agent?")
                        .contentType("quick-replies")
                        .content(List.of(Content.builder()
                                .id("1").type("quick-reply").action("message").text("yes").build(),
                                Content.builder()
                                        .id("2").type("quick-reply").action("message").text("no").build()))
                        .build()))
                .intent(SUCCESS)
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getEscalationReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Connecting to an agent")
                        .build()))
                .intent(FALL_BACK)
                .botState(BotState.FAILED).build();
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
