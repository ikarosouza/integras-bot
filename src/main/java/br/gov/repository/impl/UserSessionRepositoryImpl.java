package br.gov.repository.impl;

import br.gov.domain.UserSession;
import br.gov.repository.UserSessionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class UserSessionRepositoryImpl implements UserSessionRepository {

    @Override
    @Transactional
    public void updateOrCreateSession(String userId) {
        UserSession session = find("userId", userId).firstResult();

        if (session == null) {
            // Criar uma nova sessão para o usuário
            session = UserSession.builder()
                .userId(userId)
                .lastInteraction(LocalDateTime.now())
                .build();
            persist(session);
        } else {
            // Atualizar a última interação do usuário
            session.setLastInteraction(LocalDateTime.now());
            persist(session);
        }
    }

    @Override
    public UserSession findByUserId(String userId) {
        return find("userId", userId).firstResult();
    }
}