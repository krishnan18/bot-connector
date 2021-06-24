package com.bot.botconnector.controller;

import com.bot.botconnector.domain.BotState;
import com.bot.botconnector.domain.ChatMessage;
import com.bot.botconnector.domain.ChatResponse;
import com.bot.botconnector.domain.Content;
import com.bot.botconnector.domain.Count;
import com.bot.botconnector.domain.Message;
import com.bot.botconnector.util.MarkDownConverter;
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
    private static final String applePay = "<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">Met Apple Pay betaal je zonder extra kosten met je iPhone of Apple Watch. Je kunt je betaalpas en creditcard hiervoor gebruiken.<br /><br />Waarover gaat je vraag? <ul><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"a0ca3f8a-5bfc-428d-a48c-85120bdefc90\">Gebruiken</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"394928ef-afd7-4279-9a18-fab65e1a6070\">Verificatiecode</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"2db22765-8aaf-433b-b6e0-5419d3b7dab4\">Betaallimiet</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"42f77fd2-4ad1-4fd5-80b1-a065042eb66c\">Het werkt niet</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"02b5dced-fdfd-4599-9db8-f77416cf90f9\">Iets anders</a></li></ul> <div class=\"nw_options_end\"></div></div>";
    private static final String verificatieCode = "<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">Voor een verificatiecode moet je telefoonnummer bij ons bekend zijn. Je controleert en wijzigt dit in de Rabo App of internetbankieren op je computer.<hr />Is de verificatiecode verlopen? Verwijder dan al je passen en cards uit je Wallet en installeer ze opnieuw.<br /><br />Zo verwijder je alles:<ol><li>Ga naar &#39;Instellingen&#39; op je iPhone</li><li>Open &#39;Wallet en Apple Pay&#39;</li><li>Kies je oude pas / card en druk op &#39;Verwijder deze kaart&#39;</li><li>Herhaal dit voor iedere pas of card in je Wallet.</li></ol>Is het verwijderen gelukt? Installeer Apple Pay opnieuw. <ul><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"323c02b1-2a24-4274-b8eb-23d442d74f0f\">Check 06-nummer</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"0265df01-0063-4e63-98f4-bdf0dee51f12\">Installeer Apple Pay</a></li></ul> <div class=\"nw_options_end\"></div></div>";
    private static final String installApplePay = "<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">Check of je 06-nummer bij ons bekend is en klopt:<br /><ul><li>Log in in internetbankieren of de Rabo App</li><li>Ga naar &#39;Profiel&#39; rechtsboven</li><li>Druk op je gebruikersnaam</li><li>Kies &#39;Contactgegevens wijzigen&#39;</li></ul>Hier staat je nummer. Klopt het niet? Pas het aan met het pennetje.<br /><button><a href=\"https://bankieren.rabobank.nl/bankierenplus/deeplinking/mijn-gegevens-beheren/start\" target=\"_blank\" data-vtz-browse=\"https://bankieren.rabobank.nl/bankierenplus/deeplinking/mijn-gegevens-beheren/start\" data-vtz-link-type=\"Web\" rel=\"noopener noreferrer\">Check 06-nummer</a></button><hr />Je wijziging is na een paar dagen actief. Daarna kun je Apple Pay installeren. \n<hr />Heb je zo een antwoord op je vraag? <ul><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"83c547b6-f2ac-4af9-b2e3-552a8e5deb0c\">Ja</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"295a9f46-6316-4d13-97df-e42a24534366\">Nee</a></li></ul> <div class=\"nw_options_end\"></div></div>";

    @Autowired
    private Count count;
    @PostMapping(path = "/postUtterance")
    public ChatResponse message(@RequestPart(value = "file", required = false) ChatMessage chatMessage) {
        if(count.getCount() > 3) {
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
                return getSecondReply();
            }
            if(count.getCount() == 3){
                log.info("Second reply");
                return getThirdReply();
            }
            if(count.getCount() > 3){
                log.info("Escalation reply");
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
                        .text(MarkDownConverter.convertHtmlToMarkDown(applePay))
                        .build()))
                .intent(SUCCESS)
                .additionalProperties(true)
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getSecondReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text(MarkDownConverter.convertHtmlToMarkDown(verificatieCode)).build()))
                .intent(SUCCESS)
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getThirdReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text(MarkDownConverter.convertHtmlToMarkDown(installApplePay)).build()))
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

    private ChatResponse getFailedReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Please wait. I am looking for a colleague who is available to chat.").build()))
                .intent(FALL_BACK)
                .botState(BotState.FAILED).build();
    }
}
