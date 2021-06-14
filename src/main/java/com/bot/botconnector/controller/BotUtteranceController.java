package com.bot.botconnector.controller;

import com.bot.botconnector.domain.BotState;
import com.bot.botconnector.domain.ChatMessage;
import com.bot.botconnector.domain.ChatResponse;
import com.bot.botconnector.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class BotUtteranceController {
    private static final String SUCCESS = "success";
    private static final String FALL_BACK = "fallback";
    @PostMapping("/postUtterance")
    public ChatResponse message(@RequestBody ChatMessage chatMessage) {
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
                        .text("Hi").build()))
                .intent(SUCCESS)
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
