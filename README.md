# Personal Backend

API backend para o projeto Personal Trainer, feita em Spring Boot 4 + Java 21.

## Stack

- Java 21
- Spring Boot (Web, Data JPA, Security, Validation)
- PostgreSQL
- Flyway (migrations)
- Lombok

## Pré-requisitos

- Java 21 instalado
- Maven (ou use o `./mvnw` incluso no projeto, não precisa instalar Maven separado)
- PostgreSQL rodando localmente **ou** Docker instalado (veja seção abaixo)

## Setup

1. Clone o repositório:
```bash
   git clone https://github.com/Hyudee/personal-backend.git
   cd personal-backend
```

2. Crie o arquivo `src/main/resources/application-local.properties` com a senha do seu banco:
```properties
   spring.datasource.password=sua_senha_aqui
```
Esse arquivo é ignorado pelo Git — nunca é commitado, pois contém dado sensível.

3. Crie o banco de dados `personal_trainer` no PostgreSQL (ou pule esse passo e use o Docker Compose abaixo, que já cria automaticamente).

4. Rode a aplicação ativando o profile `local`:

   **Linux/Mac:**
```bash
   ./mvnw spring-boot:run "-Dspring-boot.run.profiles=local"
```

**Windows (PowerShell):**
```powershell
   .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

> **Recomendado se você usa o IntelliJ:** em vez de passar o parâmetro toda vez, configure o profile uma única vez direto na IDE:
> 1. Vá em **Run → Edit Configurations...**
> 2. Selecione a configuração `PersonalBackendApplication`
> 3. No campo **Active profiles**, digite `local`
> 4. Clique em **Apply** → **OK**
>
> A partir daí, todo clique em ▶️ **Run** já ativa o profile `local` automaticamente.

A API vai subir por padrão em `http://localhost:8080`.

## Banco de dados via Docker (opcional)

Se você não quiser instalar o PostgreSQL na sua máquina, use o Docker Compose incluso no projeto:

1. Copie o arquivo de exemplo e preencha com a senha:
```bash
   cp .env.example .env
```
**Atenção:** esse `.env` é usado apenas pelo `docker-compose.yaml` (senha do container PostgreSQL). Ele não tem relação com a aplicação Spring Boot, que usa o `application-local.properties` do passo anterior — são dois mecanismos separados.

2. Suba o container:
```bash
   docker compose up -d
```

Para derrubar o container:
```bash
docker compose down
```
(os dados ficam salvos no volume `postgres_data` entre reinícios — só `docker compose down -v` apaga o volume)

## Migrations

O projeto usa **Flyway** para versionamento do banco de dados. As migrations ficam em: `src/main/resources/db/migration`.

Toda alteração no schema do banco deve ser feita através de um novo arquivo de migration (ex: `V4__adiciona_tabela_x.sql`), nunca alterando uma migration já aplicada. O `ddl-auto` está configurado como `validate`, ou seja, o Hibernate só valida se as entidades batem com o schema — ele não cria nem altera tabelas automaticamente.

## Configuração de pagamento (WhatsApp)

O fluxo de cadastro gera links de pagamento via WhatsApp (cobrança e confirmação de Pix). Para isso, defina a variável de ambiente `PERSONAL_WHATSAPP` com o número do personal (com DDI/DDD, ex: `5584940028922`) antes de subir a aplicação:

```bash
export PERSONAL_WHATSAPP=5584940028922
```

Se a variável não for definida, os links de WhatsApp (`whatsappPaymentUrl` / `whatsappPixPaidUrl`) voltam como `null` na resposta da API, mas o cadastro do draft continua funcionando normalmente. As mensagens template ficam em `application.properties` (`app.payment.pay-message-template` e `app.payment.pix-paid-message-template`).

## Configuração sensível

| Arquivo | Usado por | Conteúdo |
|---|---|---|
| `application-local.properties` | Aplicação Spring Boot | `spring.datasource.password` |
| `.env` | Docker Compose | `DB_PASSWORD` |

Nenhum dos dois é commitado — ambos estão no `.gitignore`.

## Estrutura do projeto

```
src/main/java/com/personaltrainer/
├── PersonalBackendApplication.java   # classe principal
├── accountdraft/                     # pré-cadastro: pagamento pendente -> aprovação -> vira User
│   └── dto/
├── billing/
│   ├── payment/                      # chaves Pix, dias de pagamento, links de cobrança
│   └── plan/                         # planos (nome, preço, periodicidade)
├── common/                           # tratamento global de exceções
├── config/                           # configuração de segurança, beans gerais
├── email/                            # notificações por e-mail (cadastro aprovado)
├── student/                          # perfil do aluno
└── user/                             # conta de usuário (personal/aluno)

src/main/resources/
├── application.properties            # configurações gerais (sem dados sensíveis)
└── db/migration/                     # migrations do Flyway (V1 a V3 até o momento)
```

## Rodando os testes

```bash
./mvnw test
```

Os testes usam o profile `local` (via `@ActiveProfiles("local")` na classe de teste), então o `application-local.properties` precisa existir antes de rodá-los.

## Contribuindo

1. Crie uma branch a partir da `main`: `git checkout -b feature/nome-da-feature`
2. Faça suas alterações e commite
3. Abra um Pull Request descrevendo o que foi feito