package br.gov.service;

import br.gov.domain.UserSession;
import br.gov.repository.UserSessionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class SessionManager {

    @Inject
    UserSessionRepository userSessionRepository;

    private static final long SESSION_TIMEOUT_MINUTES = 5; // 5 minutos de timeout

    // Verifica se a interação é uma nova sessão
    public boolean isFirstInteraction(String userId) {
        UserSession session = userSessionRepository.findByUserId(userId);

        if (session == null) {
            return true; // Nova sessão, nunca interagiu antes
        }

        // Calcula a diferença de tempo entre a última interação e agora
        LocalDateTime now = LocalDateTime.now();
        long minutesSinceLastInteraction = ChronoUnit.MINUTES.between(session.getLastInteraction(), now);

        return minutesSinceLastInteraction > SESSION_TIMEOUT_MINUTES;
    }

    // Atualiza ou cria uma nova sessão de interação
    public void updateInteractionTime(String userId) {
        userSessionRepository.updateOrCreateSession(userId);
    }
}