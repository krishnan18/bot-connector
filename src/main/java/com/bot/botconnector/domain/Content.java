package com.bot.botconnector.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content {
    private String contentType;
    private String content;
    private QuickReply quickReply;
}
