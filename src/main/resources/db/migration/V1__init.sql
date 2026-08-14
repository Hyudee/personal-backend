-- ===== USERS =====
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL CHECK (role IN ('PERSONAL', 'STUDENT')),
                       status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE')),
                       last_login_at TIMESTAMP,
                       created_at TIMESTAMP NOT NULL DEFAULT now(),
                       updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ===== STUDENT PROFILE =====
CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
                          phone VARCHAR(20),
                          birth_date DATE,
                          active BOOLEAN NOT NULL DEFAULT true,
                          created_at TIMESTAMP NOT NULL DEFAULT now(),
                          updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ===== EXERCISES =====
CREATE TABLE exercises (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           muscle_group VARCHAR(100),
                           equipment VARCHAR(100),
                           active BOOLEAN NOT NULL DEFAULT true,
                           created_at TIMESTAMP NOT NULL DEFAULT now(),
                           updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ===== WORKOUTS =====
CREATE TABLE workouts (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          is_template BOOLEAN NOT NULL DEFAULT false,
                          created_at TIMESTAMP NOT NULL DEFAULT now(),
                          updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE workout_exercises (
                                   id BIGSERIAL PRIMARY KEY,
                                   workout_id BIGINT NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
                                   exercise_id BIGINT NOT NULL REFERENCES exercises(id),
                                   order_index INT NOT NULL,
                                   sets INT NOT NULL CHECK (sets > 0),
                                   reps VARCHAR(20) NOT NULL,
                                   load VARCHAR(20),
                                   rest_seconds INT,
                                   rpe INT,
                                   notes TEXT
);

-- ===== WORKOUT ASSIGNMENT (treino atribuído ao aluno) =====
CREATE TABLE workout_assignments (
                                     id BIGSERIAL PRIMARY KEY,
                                     workout_id BIGINT NOT NULL REFERENCES workouts(id),
                                     student_id BIGINT NOT NULL REFERENCES students(id),
                                     start_date DATE NOT NULL,
                                     end_date DATE,
                                     active BOOLEAN NOT NULL DEFAULT true,
                                     created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ===== WORKOUT LOG (execução do treino pelo aluno) =====
CREATE TABLE workout_logs (
                              id BIGSERIAL PRIMARY KEY,
                              workout_assignment_id BIGINT NOT NULL REFERENCES workout_assignments(id),
                              started_at TIMESTAMP NOT NULL,
                              finished_at TIMESTAMP,
                              duration_seconds INT
);

CREATE TABLE workout_set_logs (
                                  id BIGSERIAL PRIMARY KEY,
                                  workout_log_id BIGINT NOT NULL REFERENCES workout_logs(id) ON DELETE CASCADE,
                                  workout_exercise_id BIGINT NOT NULL REFERENCES workout_exercises(id),
                                  set_number INT NOT NULL,
                                  reps_done INT,
                                  load_done VARCHAR(20)
);

-- ===== APPOINTMENTS (agenda) =====
CREATE TABLE appointments (
                              id BIGSERIAL PRIMARY KEY,
                              student_id BIGINT NOT NULL REFERENCES students(id),
                              type VARCHAR(20) NOT NULL CHECK (type IN ('WORKOUT', 'ASSESSMENT', 'OTHER')),
                              status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'CANCELED', 'COMPLETED')),
                              scheduled_at TIMESTAMP NOT NULL,
                              notes TEXT,
                              created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ===== PLANS & BILLING (vencimentos) =====
CREATE TABLE plans (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       price NUMERIC(10,2) NOT NULL CHECK (price >= 0),
                       periodicity VARCHAR(20) NOT NULL CHECK (periodicity IN ('MONTHLY', 'QUARTERLY', 'YEARLY'))
);

CREATE TABLE billing_records (
                                 id BIGSERIAL PRIMARY KEY,
                                 student_id BIGINT NOT NULL REFERENCES students(id),
                                 plan_id BIGINT NOT NULL REFERENCES plans(id),
                                 due_date DATE NOT NULL,
                                 paid_at TIMESTAMP,
                                 status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'OVERDUE', 'PAID')),
                                 created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ===== ÍNDICES (consultas frequentes) =====
CREATE INDEX idx_students_active ON students(active);
CREATE INDEX idx_workout_assignments_student ON workout_assignments(student_id);
CREATE INDEX idx_appointments_student ON appointments(student_id);
CREATE INDEX idx_appointments_scheduled_at ON appointments(scheduled_at);
CREATE INDEX idx_billing_records_student ON billing_records(student_id);
CREATE INDEX idx_billing_records_status ON billing_records(status);