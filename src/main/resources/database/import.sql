-- Especialidades
INSERT INTO specialty (id, name) VALUES (1, 'Cardiologia');
INSERT INTO specialty (id, name) VALUES (2, 'Pediatria');
INSERT INTO specialty (id, name) VALUES (3, 'Ginecologia');
INSERT INTO specialty (id, name) VALUES (4, 'Dermatologia');
INSERT INTO specialty (id, name) VALUES (5, 'Ortopedia');

-- Médicos
INSERT INTO doctor (id, name, specialty_id) VALUES (1, 'Dr. Carlos Souza', 1);
INSERT INTO doctor (id, name, specialty_id) VALUES (2, 'Dra. Ana Pereira', 1);
INSERT INTO doctor (id, name, specialty_id) VALUES (3, 'Dr. João Silva', 2);
INSERT INTO doctor (id, name, specialty_id) VALUES (4, 'Dra. Maria Fonseca', 2);
INSERT INTO doctor (id, name, specialty_id) VALUES (5, 'Dra. Júlia Andrade', 3);
INSERT INTO doctor (id, name, specialty_id) VALUES (6, 'Dr. Ricardo Lima', 4);
INSERT INTO doctor (id, name, specialty_id) VALUES (7, 'Dra. Carla Torres', 5);

-- Agendas dos Médicos
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (1, 'Segunda-feira', '08:00', '12:00');
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (1, 'Quarta-feira', '14:00', '18:00');
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (2, 'Terça-feira', '08:00', '12:00');
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (2, 'Quinta-feira', '14:00', '18:00');
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (3, 'Segunda-feira', '08:00', '12:00');
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (3, 'Sexta-feira', '08:00', '12:00');
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (4, 'Terça-feira', '08:00', '12:00');
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (5, 'Quarta-feira', '08:00', '12:00');
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (6, 'Quinta-feira', '08:00', '12:00');
INSERT INTO schedule (doctor_id, day_of_week, start_time, end_time) VALUES (7, 'Sexta-feira', '08:00', '12:00');

-- Exames disponíveis
INSERT INTO exam (id, name, description) VALUES (1, 'Hemograma', 'Exame de sangue completo');
INSERT INTO exam (id, name, description) VALUES (2, 'Raio X', 'Exame de imagem para diagnósticos ósseos');
INSERT INTO exam (id, name, description) VALUES (3, 'Ultrassom', 'Exame de ultrassonografia');
INSERT INTO exam (id, name, description) VALUES (4, 'Eletrocardiograma', 'Exame do ritmo cardíaco');

-- Agenda para exames
INSERT INTO exam_schedule (exam_id, day_of_week, start_time, end_time) VALUES (1, 'Segunda-feira', '08:00', '12:00');
INSERT INTO exam_schedule (exam_id, day_of_week, start_time, end_time) VALUES (1, 'Quarta-feira', '13:00', '16:00');
INSERT INTO exam_schedule (exam_id, day_of_week, start_time, end_time) VALUES (2, 'Terça-feira', '09:00', '12:00');
INSERT INTO exam_schedule (exam_id, day_of_week, start_time, end_time) VALUES (3, 'Quinta-feira', '14:00', '17:00');
INSERT INTO exam_schedule (exam_id, day_of_week, start_time, end_time) VALUES (4, 'Sexta-feira', '08:00', '11:00');