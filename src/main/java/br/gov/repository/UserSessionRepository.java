package br.gov.repository;

import br.gov.domain.UserSession;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.transaction.Transactional;

public interface UserSessionRepository extends PanacheRepository<UserSession> {

  @Transactional
  void updateOrCreateSession(String userId);

  UserSession findByUserId(String userId);
}
