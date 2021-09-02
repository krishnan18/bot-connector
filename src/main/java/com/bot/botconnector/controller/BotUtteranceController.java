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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class BotUtteranceController {
    private static final String SUCCESS = "success";
    private static final String FALL_BACK = "fallback";
    private static final String applePay = "<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">Met Apple Pay betaal je zonder extra kosten met je iPhone of Apple Watch. Je kunt je betaalpas en creditcard hiervoor gebruiken.<br /><br />Waarover gaat je vraag? <ul><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"a0ca3f8a-5bfc-428d-a48c-85120bdefc90\">Gebruiken</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"394928ef-afd7-4279-9a18-fab65e1a6070\">Verificatiecode</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"2db22765-8aaf-433b-b6e0-5419d3b7dab4\">Betaallimiet</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"42f77fd2-4ad1-4fd5-80b1-a065042eb66c\">Het werkt niet</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"02b5dced-fdfd-4599-9db8-f77416cf90f9\">Iets anders</a></li></ul> <div class=\"nw_options_end\"></div></div>";
    private static final String verificatieCode = "<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">Voor een verificatiecode moet je telefoonnummer bij ons bekend zijn. Je controleert en wijzigt dit in de Rabo App of internetbankieren op je computer.<hr />Is de verificatiecode verlopen? Verwijder dan al je passen en cards uit je Wallet en installeer ze opnieuw.<br /><br />Zo verwijder je alles:<ol><li>Ga naar &#39;Instellingen&#39; op je iPhone</li><li>Open &#39;Wallet en Apple Pay&#39;</li><li>Kies je oude pas / card en druk op &#39;Verwijder deze kaart&#39;</li><li>Herhaal dit voor iedere pas of card in je Wallet.</li></ol>Is het verwijderen gelukt? Installeer Apple Pay opnieuw. <ul><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"323c02b1-2a24-4274-b8eb-23d442d74f0f\">Check 06-nummer</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"0265df01-0063-4e63-98f4-bdf0dee51f12\">Installeer Apple Pay</a></li></ul> <div class=\"nw_options_end\"></div></div>";
    private static final String installApplePay = "<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">Check of je 06-nummer bij ons bekend is en klopt:<br /><ul><li>Log in in internetbankieren of de Rabo App</li><li>Ga naar &#39;Profiel&#39; rechtsboven</li><li>Druk op je gebruikersnaam</li><li>Kies &#39;Contactgegevens wijzigen&#39;</li></ul>Hier staat je nummer. Klopt het niet? Pas het aan met het pennetje.<br /><button><a href=\"https://bankieren.rabobank.nl/bankierenplus/deeplinking/mijn-gegevens-beheren/start\" target=\"_blank\" data-vtz-browse=\"https://bankieren.rabobank.nl/bankierenplus/deeplinking/mijn-gegevens-beheren/start\" data-vtz-link-type=\"Web\" rel=\"noopener noreferrer\">Check 06-nummer</a></button><hr />Je wijziging is na een paar dagen actief. Daarna kun je Apple Pay installeren. \n<hr />Heb je zo een antwoord op je vraag? <ul><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"83c547b6-f2ac-4af9-b2e3-552a8e5deb0c\">Ja</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"295a9f46-6316-4d13-97df-e42a24534366\">Nee</a></li></ul> <div class=\"nw_options_end\"></div></div>";
    private static final String orderedListWithLink = "<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">Zo installeer je Apple Pay op je iPhone:<br /><ol><li>Open de Rabo App<br />Chat je met je iPhone? <a href=\"https://bankieren.rabobank.nl/bankierenplus/deeplinking/apple-pay/start\" target=\"_blank\" data-vtz-browse=\"https://bankieren.rabobank.nl/bankierenplus/deeplinking/apple-pay/start\" data-vtz-link-type=\"Web\" rel=\"noopener noreferrer\">Log hier in</a></li><li>Ga naar &#39;Service&#39; en kies &#39;Apple Pay&#39;</li><li>Selecteer je betaalpas of creditcard</li><li>Kies &#39;Zet je betaalpas in de Apple Wallet&#39; of &#39;Zet je creditcard in de Apple Wallet&#39;</li><li>Lees en accepteer de voorwaarden en bevestig</li></ol>Gelukt? Dan kun je nu betalen met Apple Pay. <hr />Heb je meerdere passen of cards? Herhaal deze stappen dan. Je kunt trouwens ook <a href=\"https://www.rabobank.nl/particulieren/betalen/service/betalen-en-opnemen/aan-de-slag-met-apple-pay#applewatch\" target=\"_blank\" data-vtz-browse=\"https://www.rabobank.nl/particulieren/betalen/service/betalen-en-opnemen/aan-de-slag-met-apple-pay#applewatch\" data-vtz-link-type=\"Web\" rel=\"noopener noreferrer\">met je Apple Watch betalen</a>.<br /><blockquote style=\"margin: 0 0 0 40px; border: none; padding: 0px;\"><button><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"9ade5d31-16d5-480f-b832-8670ee193d51\">Installeren lukt niet</a></button></blockquote> \n<hr />Heb je zo een antwoord op je vraag? <ul><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"83c547b6-f2ac-4af9-b2e3-552a8e5deb0c\">Ja</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"295a9f46-6316-4d13-97df-e42a24534366\">Nee</a></li></ul> <div class=\"nw_options_end\"></div></div>";
    private static final String textWithLink = "<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">Dat klinkt alsof je iets wilt weten over onze <a href=\"https://www.rabobank.nl/bedrijven/\" target=\"_blank\" data-vtz-browse=\"https://www.rabobank.nl/bedrijven/\" data-vtz-link-type=\"Web\" rel=\"noopener noreferrer\">zakelijke producten of adviezen</a>. <hr />Waar gaat je vraag over? <ul><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"fb56e6ba-4d31-4494-b173-67bae7856682\">Financiering of lease</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"ab0dee97-8063-4e5f-9c9f-f343bd780cc7\">Andere vraag</a></li></ul> <div class=\"nw_options_end\"></div></div>";
    private static final String secondaryButton = "<div onclick=\"window.inqFrame.Application.sendVALinkClicked(event);\">Vanaf 6 september kunnen clubs en verenigingen zich aanmelden voor Rabo ClubSupport. De clubs die mee kunnen doen, ontvangen vanzelf een uitnodiging.<br /><br />De stembussen gaan op 4 oktober open voor alle Rabobank-leden.<br /><button><a href=\"https://www.rabobank.nl/particulieren/leden/clubsupport/\" target=\"_blank\" data-vtz-browse=\"https://www.rabobank.nl/particulieren/leden/clubsupport/\" data-vtz-link-type=\"Web\" rel=\"noopener noreferrer\">Meer over ClubSupport</a></button><blockquote style=\"margin: 0 0 0 40px; border: none; padding: 0px;\"><button><a href=\"https://bankieren.rabobank.nl/bankierenplus/deeplinking/membership-subscription/start\" target=\"_blank\" data-vtz-browse=\"https://bankieren.rabobank.nl/bankierenplus/deeplinking/membership-subscription/start\" data-vtz-link-type=\"Web\" rel=\"noopener noreferrer\">Word Lid</a></button></blockquote> \n<hr />Heb je zo een antwoord op je vraag? <ul><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"83c547b6-f2ac-4af9-b2e3-552a8e5deb0c\">Ja</a></li><li><a href=\"#\" data-vtz-link-type=\"Dialog\" data-vtz-jump=\"295a9f46-6316-4d13-97df-e42a24534366\">Nee</a></li></ul> <div class=\"nw_options_end\"></div></div>";
    private static final String ENGAGEMENT_ID = "engagement_id";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String INTENT = "intent";
    private static final String INTENT_LEVEL = "intentLevel";
    @Autowired
    private Count count;

    @PostMapping(path = "/postUtterance")
    public ChatResponse message(@RequestBody ChatMessage chatMessage) {
        log.info(" chat message " + chatMessage);
        if("ordered list".equalsIgnoreCase(chatMessage.getInputMessage().getText())) {
            return getOrderedListReply(chatMessage);
        }
        if("text link".equalsIgnoreCase(chatMessage.getInputMessage().getText())) {
            return getTextWithLinkReply(chatMessage);
        }
        if("secondary button".equalsIgnoreCase(chatMessage.getInputMessage().getText())) {
            return getSecondaryButtonReply(chatMessage);
        }
        if("agent".equalsIgnoreCase(chatMessage.getInputMessage().getText())) {
            log.info("Escalation reply");
            return getEscalationReply(chatMessage);
        }
        if (count.getCount() > 4) {
            count.setCount(0);
        }
        count.setCount(count.getCount() + 1);
        log.info("chat message null:");
        if (count.getCount() <= 1) {
            log.info("Welcome reply");
            return getWelcomeReply(chatMessage);
        }
        if (count.getCount() == 2) {
            log.info("First reply");
            return getFirstReply(chatMessage);
        }
        if (count.getCount() == 3) {
            log.info("Second reply");
            return getSecondReply(chatMessage);
        }
        if (count.getCount() == 4) {
            log.info("Third reply");
            return getThirdReply(chatMessage);
        }
        if (count.getCount() > 4) {
            log.info("Escalation reply");
            return getEscalationReply(chatMessage);
        }
        return getFirstReply(chatMessage);
    }

    private ChatResponse getWelcomeReply(ChatMessage chatMessage) {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Hello, I am the Virtual Assistant of Rabobank. Ask your question in one sentence and I'll be happy to help you.")
                        .build()))
                .parameters(getBotSession(chatMessage))
                .intent(SUCCESS)
                //.additionalProperties(true)
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getFirstReply(ChatMessage chatMessage) {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text(MarkDownConverter.convertHtmlToMarkDown(applePay))
                        .build()))
                .intent(SUCCESS)
                .parameters(getBotSession(chatMessage))
                .additionalProperties(true)
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getSecondReply(ChatMessage chatMessage) {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text(MarkDownConverter.convertHtmlToMarkDown(verificatieCode)).build()))
                .intent(SUCCESS)
                .parameters(getBotSession(chatMessage))
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getThirdReply(ChatMessage chatMessage) {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text(MarkDownConverter.convertHtmlToMarkDown(installApplePay)).build()))
                .intent(SUCCESS)
                .parameters(getBotSession(chatMessage))
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getEscalationReply(ChatMessage chatMessage) {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Connecting to an agent")
                        .build()))
                .parameters(getBotSession(chatMessage))
                .intent(FALL_BACK)
                .botState(BotState.FAILED).build();
    }

    private ChatResponse getSecondaryButtonReply(ChatMessage chatMessage) {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text(MarkDownConverter.convertHtmlToMarkDown(secondaryButton))
                        .build()))
                .parameters(getBotSession(chatMessage))
                .intent(SUCCESS)
                .additionalProperties(true)
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getTextWithLinkReply(ChatMessage chatMessage) {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text(MarkDownConverter.convertHtmlToMarkDown(textWithLink))
                        .build()))
                .parameters(getBotSession(chatMessage))
                .intent(SUCCESS)
                .additionalProperties(true)
                .botState(BotState.COMPLETE).build();
    }

    private ChatResponse getOrderedListReply(ChatMessage chatMessage) {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text(MarkDownConverter.convertHtmlToMarkDown(orderedListWithLink))
                        .build()))
                .parameters(getBotSession(chatMessage))
                .intent(SUCCESS)
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

    private ChatResponse getFailedReply() {
        return ChatResponse.builder()
                .replymessages(List.of(Message.builder()
                        .type("Text")
                        .text("Please wait. I am looking for a colleague who is available to chat.").build()))
                .intent(FALL_BACK)
                .botState(BotState.FAILED).build();
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private Map<String, String> getBotSession(ChatMessage chatMessage) {
        Map<String, String> botSessionMap = new HashMap<>();
        if (chatMessage.getParameters() != null) {
            botSessionMap = chatMessage.getParameters().entrySet().stream()
                    .filter(k -> ENGAGEMENT_ID.equalsIgnoreCase(k.getKey())
                            || CUSTOMER_ID.equalsIgnoreCase(k.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        if (botSessionMap.isEmpty()) {
            botSessionMap = Map.of("engagementId", getUUID(), "customerId", getUUID());
        }
        return botSessionMap;
    }

    private Map<String, String> getIntentDetails() {
        Map<String, String> intentDetailsMap = Map.of(INTENT, "", INTENT_LEVEL, "");
        return intentDetailsMap;
    }
}
