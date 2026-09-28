-- ==== FLUXO DE CADASTRO COM PLANO E PAGAMENTO ====

-- Planos podem ser desativados agr (Importante ser desativar, NUNCA remover, pois temos uma referencia de historico de planos

ALTER TABLE plans DROP CONSTRAINT IF EXISTS plans_periodicity_check;
ALTER TABLE plans ADD CONSTRAINT plans_periodicity_check
    CHECK ( periodicity IN ('MONTHLY', 'QUARTERLY', 'YEARLY'));
ALTER TABLE plans ADD COLUMN active BOOLEAN NOT NULL DEFAULT true;

-- Chaves Pix aleatorias do personal
CREATE TABLE pix_keys (
    id         BIGSERIAL PRIMARY KEY,
    key_value  VARCHAR(255) NOT NULL UNIQUE,
    active     BOOLEAN       NOT NULL DEFAULT true,
    created_at TIMESTAMP     NOT NULL DEFAULT now()
);

-- Dias do pagamento, personal determina os dias
CREATE TABLE payment_days(
    day_of_month INT PRIMARY KEY CHECK ( day_of_month BETWEEN 1 AND 28),
    active BOOLEAN NOT NULL DEFAULT true
);

INSERT INTO payment_days (day_of_month) VALUES (5), (10), (15), (20);

-- Draft: Plano escolhido, dia de pagamento e chave pix

ALTER TABLE account_drafts ADD COLUMN plan_id BIGINT REFERENCES plans(id);
ALTER TABLE account_drafts ADD COLUMN payment_day INT REFERENCES payment_days(day_of_month);
ALTER TABLE account_drafts ADD COLUMN pix_key_id BIGINT REFERENCES pix_keys(id);

-- Complete os dados (Aluno)
ALTER TABLE students ADD COLUMN sex VARCHAR(10) CHECK (sex IN ('MALE', 'FEMALE'));
ALTER TABLE students ADD COLUMN payment_day INT REFERENCES payment_days(day_of_month);
ALTER TABLE students ADD COLUMN profile_completed BOOLEAN NOT NULL DEFAULT false;




