-- ==== ACCOUNT DRAFT ====
-- Fluxo de pré-cadastro: pagamento pendente -> aprovação -> vira um User de fato
CREATE TABLE account_drafts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING_PAYMENT'
        CHECK (status IN ('PENDING_PAYMENT', 'APPROVED', 'REJECTED', 'EXPIRED')),
    payment_confirmed_at TIMESTAMP,
    reviewed_at TIMESTAMP,
    reviewer_id BIGINT REFERENCES users(id),
    rejection_reason TEXT,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_account_drafts_status ON account_drafts(status);
CREATE INDEX idx_account_drafts_email ON account_drafts(email);

-- Impede múltiplos drafts pendentes de pagamento com o mesmo e-mail ao mesmo tempo
-- (drafts rejeitados/expirados com o mesmo e-mail continuam permitidos como histórico)

CREATE UNIQUE INDEX idx_account_drafts_email_pending
    ON account_drafts(email)
    WHERE status = 'PENDING_PAYMENT';
