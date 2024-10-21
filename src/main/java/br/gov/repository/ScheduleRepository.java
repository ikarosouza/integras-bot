package br.gov.repository;

import br.gov.domain.Doctor;
import br.gov.domain.Schedule;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ScheduleRepository implements PanacheRepository<Schedule> {

    public List<Schedule> findByDoctor(Doctor doctor) {
        return list("doctor", doctor);
    }
}