# Personal Backend

API backend para o projeto Personal Trainer, feita em Spring Boot 3 + Java 21.

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

2. Copie o arquivo de exemplo de variáveis de ambiente:
```bash
   cp .env.example .env
```
Depois abra o `.env` e preencha com a senha do seu banco PostgreSQL.

3. Exporte a variável de ambiente antes de rodar a aplicação:

   **Linux/Mac:**
```bash
   export DB_PASSWORD=sua_senha_aqui
```

**Windows (PowerShell):**
```powershell
   $env:DB_PASSWORD="sua_senha_aqui"
```

4. Crie o banco de dados `personal_trainer` no PostgreSQL (ou pule esse passo e use o Docker Compose abaixo, que já cria automaticamente).

5. Rode a aplicação:
```bash
   ./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`.

A API vai subir por padrão em `http://localhost:8080`.

## Banco de dados via Docker (recomendado)

Se você não quiser instalar o PostgreSQL na sua máquina, use o Docker Compose incluso no projeto:

```bash
docker compose up -d
```

Isso vai subir um container PostgreSQL já configurado com o banco `personal_trainer`, usando a mesma variável `DB_PASSWORD` do seu `.env`.

Para derrubar o container:
```bash
docker compose down
```

## Migrations

O projeto usa **Flyway** para versionamento do banco de dados. As migrations ficam em:

```
src/main/resources/db/migration
```

Toda alteração no schema do banco deve ser feita através de um novo arquivo de migration (ex: `V2__adiciona_tabela_x.sql`), nunca alterando o banco diretamente. O `ddl-auto` está configurado como `validate`, ou seja, o Hibernate só valida se as entidades batem com o schema — ele não cria nem altera tabelas automaticamente.

## Variáveis de ambiente

| Variável      | Descrição                          | Exemplo         |
|---------------|-------------------------------------|------------------|
| `DB_PASSWORD` | Senha do banco PostgreSQL           | `minhaSenha123`  |

## Estrutura do projeto

```
src/main/java/com/personaltrainer/
└── PersonalBackendApplication.java   # classe principal

src/main/resources/
├── application.properties            # configurações da aplicação
└── db/migration/                     # migrations do Flyway
```

## Rodando os testes

```bash
./mvnw test
```

## Contribuindo

1. Crie uma branch a partir da `main`: `git checkout -b feature/nome-da-feature`
2. Faça suas alterações e commite
3. Abra um Pull Request descrevendo o que foi feito