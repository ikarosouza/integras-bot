package br.gov.service;

import br.gov.utils.MessageConstants;
import br.gov.utils.State;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ChatbotNavigationService {

    private Map<String, State> userStates = new HashMap<>();

    @Inject
    MenuService menuService;

    public String handleUserInput(String userId, String userInput) {
        State state = userStates.getOrDefault(userId, State.MENU_PRINCIPAL);

        if (userInput.equalsIgnoreCase("menu principal") || userInput.equalsIgnoreCase("menu inicial")) {
            userStates.put(userId, State.MENU_PRINCIPAL);
            return menuService.getMainMenu();
        }

        if (userInput.equalsIgnoreCase("voltar")) {
            return handleBackOption(userId);
        }

        switch (state) {
            case MENU_PRINCIPAL:
                return menuService.processMenuOption(userId, userInput, userStates);
            case LISTA_ESPECIALIDADES:
                return menuService.processSpecialtySelection(userId, userInput, userStates);
            case LISTA_MEDICOS:
                return menuService.processDoctorSelection(userId, userInput, userStates);
            case LISTA_AGENDAS:
                return menuService.processScheduleSelection(userId, userInput, userStates);
            case LISTA_EXAMES:
                return menuService.processExamSelection(userId, userInput, userStates);
            default:
                return MessageConstants.OPCAO_INVALIDA + " " + menuService.getMainMenu();
        }
    }

    private String handleBackOption(String userId) {
        State state = userStates.getOrDefault(userId, State.MENU_PRINCIPAL);

        switch (state) {
            case LISTA_ESPECIALIDADES:
                userStates.put(userId, State.MENU_PRINCIPAL);
                return menuService.getMainMenu();
            case LISTA_MEDICOS:
                userStates.put(userId, State.LISTA_ESPECIALIDADES);
                return menuService.listSpecialties();
            case LISTA_AGENDAS:
                userStates.put(userId, State.LISTA_MEDICOS);
                return menuService.listDoctors(menuService.getCurrentSpecialty(userId));
            default:
                return menuService.getMainMenu();
        }
    }
}