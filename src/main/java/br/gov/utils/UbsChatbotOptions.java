package br.gov.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UbsChatbotOptions {

    CONSULTA_HORARIOS(1, "Consultar horários de atendimento"),
    CONSULTAR_ESPECIALIDADE(2, "Consultar especialidades disponíveis"),
    CONSULTAR_EXAME(3, "Consultar exames disponíveis"),
    INFORMACOES_SERVICOS(4, "Informações sobre os serviços disponíveis"),
    FALAR_ATENDENTE(5, "Falar com um atendente"),
    LOCALIZACAO_UBS(6, "Localização e endereço da UBS"),
    HORARIOS_VACINACAO(7, "Horários de vacinação");

    private final int id;
    private final String description;

    public static UbsChatbotOptions fromId(int id) {
        for (UbsChatbotOptions option : values()) {
            if (option.getId() == id) {
                return option;
            }
        }
        throw new IllegalArgumentException("Nenhuma opção encontrada para o ID: " + id);
    }
}