# BankFlow

API REST de um sistema bancário desenvolvido com Java e Spring Boot. Contempla gerenciamento de usuários, contas, agências, gerentes, cartões, chaves Pix, boletos, investimentos, faturas, empréstimos, parcelas e lançamentos, com autenticação JWT e controle de acesso.

## Tecnologias

- Java 21 e Spring Boot 4.2.0
- Spring Web MVC, Spring Data JPA, Spring Security e Bean Validation
- JWT (Auth0 / JJWT) e BCrypt
- PostgreSQL 17 e Flyway
- Maven
- Docker e Docker Compose
- Bucket4j
- Micrometer e Prometheus
- Springdoc OpenAPI
- Hibernate Envers e ShedLock

## Requisitos

- JDK 21
- Maven (ou Maven Wrapper)
- PostgreSQL 17
- Docker e Docker Compose (para execução em containers)

## Configuração e execução

Crie o banco de dados `bankflow` no PostgreSQL e configure as variáveis de ambiente:

```bash
export DB_USER=postgres
export DB_PASSWORD=sua_senha
export JWT_SECRET=uma_chave_secreta
```

Inicie a aplicação:

```bash
./mvnw spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

Para executar os testes:

```bash
./mvnw test
```

## Execução com Docker

O `Dockerfile` e o `compose.yaml` executam a API e o PostgreSQL juntos. Crie um arquivo `.env` na raiz:

```env
DB_USER=postgres
DB_PASSWORD=sua_senha
JWT_SECRET=uma_chave_secreta
```

Execute:

```bash
docker compose up --build
```

A API fica disponível em `http://localhost:8080`.

Para parar os containers:

```bash
docker compose down      # mantém os dados
docker compose down -v   # remove também os dados do banco
```

## Autenticação

O sistema utiliza Spring Security e JWT. As senhas são armazenadas com BCrypt. Envie o token nas rotas protegidas:

```http
Authorization: Bearer <accessToken>
```

Endpoints de autenticação:

- `POST /usuarios/auth/registrar` — registro de usuário
- `POST /usuarios/auth/logar` — autenticação
- `POST /usuarios/auth/refresh` — renovação de token

O projeto possui regras de autorização para operações administrativas, gerentes e usuários autenticados. Tokens e credenciais devem ser mantidos em segurança e nunca publicados no repositório.

## Principais rotas

| Recurso | Rotas |
|---|---|
| Usuários | `/usuarios`, `/usuarios/me`, `/usuarios/agencia`, `/usuarios/auth/registrar`, `/usuarios/auth/logar`, `/usuarios/auth/refresh` |
| Agências | `/agencias` |
| Gerentes | `/gerentes`, `/gerentes/me`, `/gerentes/{id}`, `/gerentes/{id}/status` |
| Contas | `/contas`, `/contas/minha`, `/contas/agencia`, `/contas/{id}`, `/contas/agencia/{id}` |
| Endereços | `/enderecos`, `/enderecos/me`, `/enderecos/me/{id}`, `/enderecos/{id}` |
| Chaves Pix | `/chavepix`, `/chavepix/minhas`, `/chavepix/minhas/{id}`, `/chavepix/minhas/{id}/desativar`, `/chavepix/gerente` |
| Cartões | `/cartaos`, `/cartaos/me`, `/cartaos/gerente`, `/cartaos/{id}`, `/cartaos/gerente/{id}` |
| Transações | `/transacoes`, `/transacoes/minhas`, `/transacoes/gerente` |
| Boletos | `/boletos`, `/boletos/me`, `/boletos/{id}/me`, `/boletos/gerente` |
| Investimentos | `/investimentos`, `/investimentos/meus`, `/investimentos/meus/{id}`, `/investimentos/gerente` |
| Faturas | `/faturas`, `/faturas/minhas`, `/faturas/gerente`, `/faturas/{id}` |
| Empréstimos | `/emprestimos`, `/emprestimos/meus`, `/emprestimos/meus/{id}`, `/emprestimos/gerente`, `/emprestimos/gerente/{id}` |
| Parcelas | `/parcelas`, `/parcelas/minhas`, `/parcelas/gerente`, `/parcelas/{id}` |
| Lançamentos | `/lancamentos`, `/lancamentos/meus`, `/lancamentos/gerente` |

As listagens utilizam paginação do Spring Data, com parâmetros como `page`, `size` e `sort`. As regras de acesso estão centralizadas na configuração do Spring Security.

## Observabilidade e documentação

O projeto inclui Actuator, Micrometer e Prometheus, Também utiliza Hibernate Envers para auditoria e ShedLock para coordenação de tarefas agendadas.

## Estrutura

```text
src/main/java/com/bankflow/joao/
├── config/       # Configurações, segurança e filtros
├── controller/   # Endpoints REST
├── domain/       # Entidades e regras de domínio
├── dto/          # DTOs de request e response
├── mapper/       # Conversão entre objetos
├── repository/   # Repositórios JPA
└── service/      # Regras de negócio

src/main/resources/
└── application.properties
Dockerfile
compose.yaml
```

## Autor

**João Vitor de Jesus Alves** · [GitHub](https://github.com/JoaoVitorAlves-dev)
