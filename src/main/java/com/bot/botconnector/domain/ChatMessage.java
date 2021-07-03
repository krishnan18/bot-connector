package com.bot.botconnector.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage {
    private String botId;
    private String botVersion;
    private Map<String,String> parameters;
    private Message inputMessage;
    private String languageCode;
    private String botSessionId;
    private String botSessionTimeout;
    private String genesysConversationId;
}
