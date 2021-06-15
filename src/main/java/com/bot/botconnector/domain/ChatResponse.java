package com.bot.botconnector.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatResponse {
    private List<Message> replymessages;
    private BotState botState;
    private String intent;
    private Map<String,String> parameters;
    private boolean additionalProperties;
}
