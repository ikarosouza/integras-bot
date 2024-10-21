package br.gov.repository;

import br.gov.domain.Exam;
import br.gov.domain.ExamSchedule;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ExamScheduleRepository implements PanacheRepository<ExamSchedule> {

    public List<ExamSchedule> findByExam(Exam exam) {
        return list("exam", exam);
    }
}