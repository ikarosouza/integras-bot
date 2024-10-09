package br.gov.service;

import br.gov.domain.TwilioMessage;
import br.gov.domain.UbsChatbotOptions;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class WhatsappService {

    @Inject
    SessionManager sessionManager; // Gerenciador de sessões

    @ConfigProperty(name = "twilio.account_sid")
    String accountSid;

    @ConfigProperty(name = "twilio.auth_token")
    String authToken;

    @ConfigProperty(name = "twilio.whatsapp_from")
    String whatsappFrom;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    // Processa a mensagem recebida do Twilio
    public void processReceivedMessage(TwilioMessage message) {
        String userId = message.getFrom();
        boolean isFirstInteraction = sessionManager.isFirstInteraction(userId);

        // Gera a resposta com base na interação
        String responseMessage = generateResponse(message.getBody(), isFirstInteraction);

        // Atualiza o tempo da última interação do usuário
        sessionManager.updateInteractionTime(userId);

        // Enviar uma resposta ao usuário
        sendMessage(userId, responseMessage);
    }

    // Método para enviar a mensagem ao usuário
    public void sendMessage(String to, String body) {
        Message message = Message.creator(
            new PhoneNumber(to),
            new PhoneNumber(whatsappFrom),
            body
        ).create();
        System.out.println("Mensagem enviada: " + message.getSid());
    }

    // Gera a resposta ao usuário com base se é a primeira interação ou não
    public String generateResponse(String userInput, boolean isFirstInteraction) {
        return checkAndSendWelcomeMessage(userInput, isFirstInteraction);
    }

    // Verificar se a mensagem é de boas-vindas
    public String checkAndSendWelcomeMessage(String userInput, boolean isFirstInteraction) {
        if (isFirstInteraction || userInput.equalsIgnoreCase("iniciar") || userInput.equalsIgnoreCase("olá") || userInput.equalsIgnoreCase("oi")) {
            return getWelcomeMessage();
        }
        return processUserInput(userInput);  // Processa a mensagem normalmente se não for de boas-vindas
    }

    // Gera a mensagem de boas-vindas
    private String getWelcomeMessage() {
        StringBuilder message = new StringBuilder();
        // Mensagem de boas-vindas
        message.append("Bem-vindo à UBS Manoel Salustino! "
            + "Aqui estão as opções disponíveis:\n");

        // Listar as opções do enum
        for (UbsChatbotOptions option : UbsChatbotOptions.values()) {
            message.append(option.getId())
                .append(" - ")
                .append(option.getDescription())
                .append("\n");
        }

        return message.toString();
    }

    // Processa a entrada do usuário para identificar a opção escolhida
    public String processUserInput(String userInput) {
        try {
            int optionId = Integer.parseInt(userInput);
            for (UbsChatbotOptions option : UbsChatbotOptions.values()) {
                if (option.getId() == optionId) {
                    return handleOption(option);
                }
            }
        } catch (NumberFormatException e) {
            for (UbsChatbotOptions option : UbsChatbotOptions.values()) {
                if (userInput.equalsIgnoreCase(option.name())) {
                    return handleOption(option);
                }
            }
        }

        return "Desculpe, não entendi sua solicitação. Por favor, escolha uma das opções.";
    }

    private String handleOption(UbsChatbotOptions option) {
        switch (option) {
            case CONSULTA_HORARIOS:
                return "Os horários de atendimento são de segunda a sexta das 08:00 às 17:00.";
            case AGENDAR_CONSULTA:
                return "Para agendar uma consulta, por favor forneça o seu nome completo e a data desejada.";
            case AGENDAR_EXAME:
                return "Para agendar um exame, informe o tipo de exame e sua data preferida.";
            case INFORMACOES_SERVICOS:
                return "A UBS oferece consultas médicas, exames de rotina e vacinação.";
            case FALAR_ATENDENTE:
                return "Você será conectado com um atendente em breve.";
            case LOCALIZACAO_UBS:
                return "A UBS está localizada na Rua Exemplo, número 123.";
            case HORARIOS_VACINACAO:
                return "Os horários de vacinação são das 09:00 às 16:00.";
            default:
                return "Opção inválida.";
        }
    }
}