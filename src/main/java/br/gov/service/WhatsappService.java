package br.gov.service;

import br.gov.domain.Specialty;
import br.gov.domain.TwilioMessage;
import br.gov.domain.UbsChatbotOptions;
import br.gov.repository.SpecialtyRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class WhatsappService {

    @Inject
    SessionManager sessionManager;

    @Inject
    SpecialtyRepository specialtyRepository; // Repositório de especialidades

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

    public void processReceivedMessage(TwilioMessage message) {
        String userId = message.getFrom();
        boolean isFirstInteraction = sessionManager.isFirstInteraction(userId);

        String responseMessage = generateResponse(message.getBody(), isFirstInteraction);

        sessionManager.updateInteractionTime(userId);

        sendMessage(userId, responseMessage);
    }

    public void sendMessage(String to, String body) {
        Message message = Message.creator(
            new PhoneNumber(to),
            new PhoneNumber(whatsappFrom),
            body
        ).create();
        System.out.println("Mensagem enviada: " + message.getSid());
    }

    public String generateResponse(String userInput, boolean isFirstInteraction) {
        if (isFirstInteraction || userInput.equalsIgnoreCase("iniciar") || userInput.equalsIgnoreCase("olá") || userInput.equalsIgnoreCase("oi")) {
            return getWelcomeMessage();
        }
        return processUserInput(userInput);
    }

    private String getWelcomeMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Bem-vindo à UBS Manoel Salustino! Aqui estão as opções disponíveis:\n");

        for (UbsChatbotOptions option : UbsChatbotOptions.values()) {
            message.append(option.getId()).append(" - ").append(option.getDescription()).append("\n");
        }

        return message.toString();
    }

    public String processUserInput(String userInput) {
        try {
            int optionId = Integer.parseInt(userInput);
            for (UbsChatbotOptions option : UbsChatbotOptions.values()) {
                if (option.getId() == optionId) {
                    if (option == UbsChatbotOptions.AGENDAR_CONSULTA) {
                        return listSpecialties();
                    }
                    return handleOption(option);
                }
            }
        } catch (NumberFormatException e) {
            if (userInput.equalsIgnoreCase("voltar")) {
                return getWelcomeMessage(); // Voltar à lista inicial
            }
            // Outra lógica, se necessário
        }
        return "Desculpe, não entendi sua solicitação. Por favor, escolha uma das opções.";
    }

    private String listSpecialties() {
        List<Specialty> specialties = specialtyRepository.listAllSpecialties();
        StringBuilder response = new StringBuilder("Escolha uma especialidade:\n");

        for (int i = 0; i < specialties.size(); i++) {
            response.append(i + 1).append(" - ").append(specialties.get(i).getName()).append("\n");
        }

        response.append("Digite 'voltar' para retornar à lista inicial.");
        return response.toString();
    }

    private String handleOption(UbsChatbotOptions option) {
        switch (option) {
            case CONSULTA_HORARIOS:
                return "Os horários de atendimento são de segunda a sexta das 08:00 às 17:00.";
            case AGENDAR_CONSULTA:
                return listSpecialties(); // Redireciona para a lista de especialidades
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