package br.gov.controller;

import br.gov.domain.TwilioMessage;
import br.gov.service.WhatsappService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;

@Path("/whatsapp")
@AllArgsConstructor
public class WhatsappBotResource {

    @Inject
    WhatsappService whatsappService;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Certifique-se de consumir o tipo correto
    public void receiveMessage(@FormParam("Body") String body,
        @FormParam("From") String from,
        @FormParam("To") String to,
        @FormParam("AccountSid") String accountSid) {
        var message = new TwilioMessage();
        message.setBody(body);
        message.setTo(to);
        message.setFrom(from);
        message.setAccountSid(accountSid);

        // Processar a mensagem recebida
        whatsappService.processReceivedMessage(message);
    }
}