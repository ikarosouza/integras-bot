package br.gov.repository;

import br.gov.domain.Exam;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExamRepository implements PanacheRepository<Exam> {
}