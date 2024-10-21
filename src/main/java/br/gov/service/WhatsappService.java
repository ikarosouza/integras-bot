package br.gov.service;

import br.gov.domain.TwilioMessage;
import br.gov.utils.UbsChatbotOptions;
import br.gov.utils.MessageConstants;
import br.gov.domain.User;
import br.gov.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class WhatsappService {

    @ConfigProperty(name = "twilio.account_sid")
    String accountSid;

    @ConfigProperty(name = "twilio.auth_token")
    String authToken;

    @ConfigProperty(name = "twilio.whatsapp_from")
    String whatsappFrom;

    @Inject
    ChatbotNavigationService navigationService;

    @Inject
    SessionManager sessionManager;

    @Inject
    UserRepository userRepository;

    public void processReceivedMessage(TwilioMessage message) {
        String userId = message.getFrom();
        User user = userRepository.findByWhatsappId(userId);

        String responseMessage;

        if (user == null && !sessionManager.isAwaitingName(userId)) {
            // Primeira mensagem: solicita o nome do usuário
            responseMessage = MessageConstants.SOLICITAR_NOME;
            sessionManager.updateInteractionTime(userId);
            sessionManager.setAwaitingName(userId, true); // Indica que está aguardando o nome
        } else if (sessionManager.isAwaitingName(userId)) {
            // Se estamos aguardando o nome, chamamos addNewUser para salvar
            addNewUser(userId, message.getBody());
            responseMessage = getWelcomeMessage(message.getBody());
            sessionManager.setAwaitingName(userId, false); // Remove o estado de espera do nome
        } else if (sessionManager.isFirstInteraction(userId) || message.getBody().equalsIgnoreCase("ola")) {
            responseMessage = getWelcomeMessage(user.getName());
        } else {
            responseMessage = navigationService.handleUserInput(userId, message.getBody());
        }

        sendMessage(userId, responseMessage);
    }

    @Transactional
    public void addNewUser(String userId, String userName) {
        User user = new User();
        user.setWhatsappId(userId);
        user.setName(userName);
        userRepository.persist(user);
    }

    public void sendMessage(String to, String body) {
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
            new PhoneNumber(to),
            new PhoneNumber(whatsappFrom),
            body
        ).create();
        System.out.println("Mensagem enviada: " + message.getSid());
    }

    private String getWelcomeMessage(String userName) {
        StringBuilder message = new StringBuilder();
        message.append(String.format(MessageConstants.BEM_VINDO + ", %s! "
            + MessageConstants.MENU_PRINCIPAL + "\n", userName));

        for (UbsChatbotOptions option : UbsChatbotOptions.values()) {
            message.append(option.getId()).append(" - ").append(option.getDescription()).append("\n");
        }

        return message.toString();
    }
}