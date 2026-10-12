-- Remove os planos que nenhum cadastro/cobrança referencia
DELETE FROM plans
WHERE id NOT IN (SELECT plan_id FROM account_drafts WHERE plan_id IS NOT NULL)
  AND id NOT IN (SELECT plan_id FROM billing_records);

-- Os que sobraram (já referenciados) ficam desativados para preservar o histórico
UPDATE plans SET active = false;

-- Planos atuais
INSERT INTO plans (name, price, periodicity, active) VALUES
     ('Básico',        300.00, 'MONTHLY', true),
     ('Intermediário', 600.00, 'MONTHLY', true),
     ('Premium',       900.00, 'MONTHLY', true);