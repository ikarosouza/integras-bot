package br.gov.service;

import br.gov.domain.Doctor;
import br.gov.domain.Exam;
import br.gov.domain.ExamSchedule;
import br.gov.domain.Specialty;
import br.gov.repository.ExamRepository;
import br.gov.repository.ExamScheduleRepository;
import br.gov.utils.MessageConstants;
import br.gov.utils.State;
import br.gov.utils.UbsChatbotOptions;
import br.gov.repository.DoctorRepository;
import br.gov.repository.ScheduleRepository;
import br.gov.repository.SpecialtyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MenuService {

    @Inject
    SpecialtyRepository specialtyRepository;

    @Inject
    DoctorRepository doctorRepository;

    @Inject
    ScheduleRepository scheduleRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    ExamScheduleRepository examScheduleRepository;

    private final Map<String, Specialty> currentSpecialty = new HashMap<>();
    private final Map<String, Doctor> currentDoctor = new HashMap<>();
    private final Map<String, Exam> currentExam = new HashMap<>();

    public String getMainMenu() {
        StringBuilder menu = new StringBuilder(MessageConstants.MENU_PRINCIPAL + "\n");
        for (UbsChatbotOptions option : UbsChatbotOptions.values()) {
            menu.append(option.getId()).append(" - ").append(option.getDescription()).append("\n");
        }
        return menu.toString();
    }

    public String processMenuOption(String userId, String userInput, Map<String, State> userStates) {
        try {
            int optionId = Integer.parseInt(userInput);
            UbsChatbotOptions option = UbsChatbotOptions.fromId(optionId);

            if (option == UbsChatbotOptions.CONSULTAR_ESPECIALIDADE) {
                userStates.put(userId, State.LISTA_ESPECIALIDADES);
                return listSpecialties();
            }

            return handleOption(option);
        } catch (NumberFormatException e) {
            return MessageConstants.OPCAO_INVALIDA + " " + getMainMenu();
        }
    }

    public String processSpecialtySelection(String userId, String userInput, Map<String, State> userStates) {
        try {
            int specialtyId = Integer.parseInt(userInput) - 1;
            List<Specialty> specialties = specialtyRepository.listAllSpecialties();

            if (specialtyId >= 0 && specialtyId < specialties.size()) {
                Specialty specialty = specialties.get(specialtyId);
                currentSpecialty.put(userId, specialty);
                userStates.put(userId, State.LISTA_MEDICOS);
                return listDoctors(specialty);
            } else {
                return MessageConstants.OPCAO_INVALIDA + " " + listSpecialties();
            }
        } catch (NumberFormatException e) {
            return MessageConstants.OPCAO_INVALIDA + " " + listSpecialties();
        }
    }

    public String processDoctorSelection(String userId, String userInput, Map<String, State> userStates) {
        try {
            int doctorId = Integer.parseInt(userInput) - 1;
            List<Doctor> doctors = doctorRepository.findBySpecialty(currentSpecialty.get(userId));

            if (doctorId >= 0 && doctorId < doctors.size()) {
                Doctor doctor = doctors.get(doctorId);
                currentDoctor.put(userId, doctor);
                userStates.put(userId, State.LISTA_AGENDAS);
                return listSchedules(doctor);
            } else {
                return MessageConstants.OPCAO_INVALIDA + " " + listDoctors(currentSpecialty.get(userId));
            }
        } catch (NumberFormatException e) {
            return MessageConstants.OPCAO_INVALIDA + " " + listDoctors(currentSpecialty.get(userId));
        }
    }

    public String processScheduleSelection(String userId, String userInput, Map<String, State> userStates) {
        return MessageConstants.SELECAO_CONCLUIDA;
    }

    public String listSpecialties() {
        List<Specialty> specialties = specialtyRepository.listAllSpecialties();
        StringBuilder response = new StringBuilder("\nEscolha uma especialidade:\n");

        for (int i = 0; i < specialties.size(); i++) {
            response.append(i + 1).append(" - ").append(specialties.get(i).getName()).append("\n");
        }

        response.append(MessageConstants.VOLTAR_MENU_PRINCIPAL);
        return response.toString();
    }

    public String listDoctors(Specialty specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialty(specialty);
        StringBuilder response = new StringBuilder("Escolha um médico da especialidade ").append(specialty.getName()).append(":\n");

        for (int i = 0; i < doctors.size(); i++) {
            response.append(i + 1).append(" - ").append(doctors.get(i).getName()).append("\n");
        }

        response.append(MessageConstants.VOLTAR_LISTA_ESPECIALIDADES);
        return response.toString();
    }

    public String listSchedules(Doctor doctor) {
        StringBuilder response = new StringBuilder("Agenda do Dr(a). ").append(doctor.getName()).append(":\n");
        scheduleRepository.findByDoctor(doctor).forEach(schedule -> {
            response.append(schedule.getDayOfWeek())
                .append(" - ")
                .append(schedule.getStartTime())
                .append(" às ")
                .append(schedule.getEndTime())
                .append("\n");
        });

        response.append(MessageConstants.VOLTAR_LISTA_MEDICOS);
        return response.toString();
    }

    public Specialty getCurrentSpecialty(String userId) {
        return currentSpecialty.get(userId);
    }

    public String listAvailableExams() {
        List<Exam> exams = examRepository.listAll();
        StringBuilder response = new StringBuilder(MessageConstants.LISTA_EXAMES_DISPONIVEIS + "\n");

        for (Exam exam : exams) {
            response.append("- ").append(exam.getName());
            if (exam.getDescription() != null) {
                response.append(": ").append(exam.getDescription());
            }
            response.append("\n");
        }

        response.append(MessageConstants.VOLTAR_MENU_PRINCIPAL);
        return response.toString();
    }

    public String listExamSchedule(String examName) {
        Exam exam = examRepository.find("name", examName).firstResult();
        if (exam == null) {
            return MessageConstants.EXAM_NOT_FOUND;
        }

        List<ExamSchedule> schedules = examScheduleRepository.findByExam(exam);
        StringBuilder response = new StringBuilder(String.format(MessageConstants.EXAM_SCHEDULE, exam.getName()));

        for (ExamSchedule schedule : schedules) {
            response.append(schedule.getDayOfWeek()).append(" - ")
                .append(schedule.getStartTime()).append(" às ")
                .append(schedule.getEndTime()).append("\n");
        }

        response.append(MessageConstants.VOLTAR_MENU_PRINCIPAL);
        return response.toString();
    }

    public String processExamSelection(String userId, String userInput, Map<String, State> userStates) {
        try {
            var examId = Long.parseLong(userInput);
            var exam = examRepository.findById(examId);

            if (examId >= 0 && exam != null) {
                userStates.put(userId, State.LISTA_AGENDA_EXAMES);
                currentExam.put(userId, exam);
                return listExamSchedule(exam.getName());
            } else {
              assert exam != null;
              return MessageConstants.OPCAO_INVALIDA + " " + listExamSchedule(exam.getName());
            }
        } catch (NumberFormatException e) {
            return MessageConstants.OPCAO_INVALIDA + " " + listExamSchedule(currentExam.get(userId).getName());
        }
    }

    public String handleOption(UbsChatbotOptions option) {
        switch (option) {
            case CONSULTA_HORARIOS:
                return MessageConstants.CONSULTA_HORARIOS;
            case CONSULTAR_ESPECIALIDADE:
                return listSpecialties();
            case CONSULTAR_EXAME:
                return listAvailableExams();
            case INFORMACOES_SERVICOS:
                return MessageConstants.INFORMACOES_SERVICOS;
            case FALAR_ATENDENTE:
                return MessageConstants.FALAR_ATENDENTE;
            case LOCALIZACAO_UBS:
                return MessageConstants.LOCALIZACAO_UBS;
            case HORARIOS_VACINACAO:
                return MessageConstants.HORARIOS_VACINACAO;
            default:
                return MessageConstants.OPCAO_INVALIDA;
        }
    }
}