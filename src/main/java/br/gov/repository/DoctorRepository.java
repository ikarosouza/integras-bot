package br.gov.repository;

import br.gov.domain.Doctor;
import br.gov.domain.Specialty;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DoctorRepository implements PanacheRepository<Doctor> {

    public List<Doctor> findBySpecialty(Specialty specialty) {
        return list("specialty", specialty);
    }
}