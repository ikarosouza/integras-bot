package br.gov.repository;

import br.gov.domain.Specialty;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SpecialtyRepository implements PanacheRepository<Specialty> {

    public List<Specialty> listAllSpecialties() {
        return listAll();
    }
}