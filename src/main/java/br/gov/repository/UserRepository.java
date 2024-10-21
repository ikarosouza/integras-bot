package br.gov.repository;

import br.gov.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findByWhatsappId(String whatsappId) {
        return find("whatsappId", whatsappId).firstResult();
    }
}