package br.gov.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UbsChatbotOptions {

    CONSULTA_HORARIOS(1, "Consulta horários de atendimento"),
    AGENDAR_CONSULTA(2, "Agendar uma consulta"),
    AGENDAR_EXAME(3, "Agendar um exame"),
    INFORMACOES_SERVICOS(4, "Informações sobre os serviços disponíveis"),
    FALAR_ATENDENTE(5, "Falar com um atendente"),
    LOCALIZACAO_UBS(6, "Localização e endereço da UBS"),
    HORARIOS_VACINACAO(7, "Horários de vacinação");

    private final int id;
    private final String description;
}