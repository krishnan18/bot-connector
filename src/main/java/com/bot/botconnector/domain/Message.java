package com.bot.botconnector.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String type;
    private String text;
    private String contentType;
    private List<Content> content;

}
