# 🐾 FutureVet — Plataforma de Gestão Veterinária

> Aplicação web desenvolvida com **Spring Boot** como parte do Challenge FIAP 2026 (2º Semestre) em parceria com a **Clyvo Vet**, com foco em saúde contínua e bem-estar animal.

---

## 📋 Sobre o Projeto

O **FutureVet** é uma plataforma web que permite a clínicas veterinárias e tutores gerenciarem de forma centralizada o histórico de saúde dos pets, agendamento de consultas e controle de vacinas. O sistema conta com autenticação segura e controle de acesso baseado em perfis de usuário (ADMIN e TUTOR).

### Funcionalidades principais

- ✅ Cadastro e gerenciamento de tutores e pets
- ✅ Agendamento de consultas, com confirmação, cancelamento e conclusão pela clínica
- ✅ Carteira de vacinação com controle de doses, reforços e histórico
- ✅ Autenticação com Spring Security (dois perfis: ADMIN e TUTOR)
- ✅ API REST com autenticação JWT, documentada com Swagger
- ✅ Controle de versão do banco de dados com Flyway
- ✅ Interface web responsiva com Thymeleaf

---

## 🎬 Vídeo Demonstrativo

[![Assistir no YouTube](https://img.shields.io/badge/YouTube-Assistir%20Demo-red?style=for-the-badge&logo=youtube)](https://youtu.be/jWtMZDGaDf8)

> Demonstração completa das funcionalidades da aplicação web (máx. 10 min.)

---

## 🚀 Deploy

[![Acessar Aplicação](https://img.shields.io/badge/Render-Aplicação%20Online-46E3B7?style=for-the-badge&logo=render)](https://challenge-sprint3-java.onrender.com)

> ⚠️ O plano gratuito do Render pode levar até **60 segundos** para iniciar após um período de inatividade.

---

## 👥 Integrantes

| Nome | RM |
|------|----|
| João Victor Caetano Alves da Silva | RM562074 |
| João Victor Bueno Castelini da Silva | RM564115 |
| Ryan Vetoriano | RM565667 |
| Felipe Furlanetto | RM562766 |
| Raul Rezende Iemini Aguiar | RM564002 |

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2.5 | Framework web |
| Spring Security | 6.x | Autenticação e autorização |
| Spring JDBC (JdbcTemplate) | 6.x | Persistência de dados |
| JJWT | 0.12.6 | Geração e validação do token JWT da API |
| Springdoc OpenAPI | 2.5.0 | Documentação da API (Swagger UI) |
| Flyway | 9.x | Migrations do banco de dados |
| Thymeleaf | 3.x | Template engine (frontend) |
| Oracle Database | 19c | Banco de dados em produção |
| H2 | 2.x | Banco de dados em desenvolvimento |
| Maven | 3.8+ | Gerenciamento de dependências |

---

## ⚙️ Como Executar Localmente

### Pré-requisitos

- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- Git

### 1. Clone o repositório

```bash
git clone https://github.com/joaocaetano1310/Challenge_Sprint3_Java.git
cd Challenge_Sprint3_Java
```

### 2. Execute com banco H2 (sem configuração adicional)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

### 3. Acesse no navegador

```
http://localhost:8080
```

### 4. Executar os testes

```bash
mvn test
```

---

## 🔐 Credenciais de Acesso

Contas criadas automaticamente ao rodar no perfil `demo`:

| Perfil | E-mail | Senha |
|--------|--------|-------|
| **ADMIN** (clínica) | clinica@futurevet.local | Futurevet123! |
| **TUTOR** | tutor@futurevet.local | Futurevet123! |
| **TUTOR** (segundo tutor) | outro@futurevet.local | Futurevet123! |

> O perfil **ADMIN** representa a clínica e tem acesso completo ao sistema, incluindo o gerenciamento de usuários e a confirmação, o cancelamento e a conclusão das consultas. O perfil **TUTOR** acessa apenas os próprios pets, vacinas e consultas.
>
> No perfil `oracle` (produção) esses dados de demonstração não são carregados: crie sua conta em `/registro`.

---

## 🔌 API REST

A aplicação também expõe uma API REST em `/api`, protegida por token JWT, utilizada pelo aplicativo mobile do projeto.

- **Documentação (Swagger UI):** http://localhost:8080/swagger-ui/index.html
- **Especificação OpenAPI:** http://localhost:8080/v3/api-docs

### Autenticação

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"tutor@futurevet.local","senha":"Futurevet123!"}'
```

A resposta traz o token, que deve ser enviado nas demais requisições:

```bash
curl http://localhost:8080/api/animais \
  -H "Authorization: Bearer SEU_TOKEN"
```

### Principais endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/auth/login` | Autentica e devolve o token JWT |
| POST | `/api/auth/registro` | Cadastra um novo tutor |
| GET / POST / PUT / DELETE | `/api/animais` | CRUD de animais |
| GET / POST / PUT / DELETE | `/api/vacinas` | Carteira de vacinação |
| POST | `/api/vacinas/{id}/reforco` | Registra o reforço de uma dose |
| GET / POST / PUT | `/api/consultas` | Agenda de consultas |
| POST | `/api/consultas/{id}/confirmar` · `/cancelar` · `/concluir` | Ações da clínica |

---

## 🗄️ Banco de Dados

O projeto utiliza **Flyway** para controle de versão do banco. As migrations são executadas automaticamente na inicialização.

| Versão | Tipo | Descrição |
|--------|------|-----------|
| `V1__estrutura_original.sql` | SQL | Estrutura original das tabelas |
| `V2__suporte_aplicacao.java` | Java | Perfis de usuário, status das consultas, vínculo de reforços, sequences e índices |
| `V3__proteger_senhas.java` | Java | Conversão das senhas existentes para hash BCrypt |

### Perfis disponíveis

| Perfil | Banco | Uso |
|--------|-------|-----|
| `demo` | H2 (em memória) | Desenvolvimento local |
| `oracle` | Oracle 19c | Produção |

### Executar com Oracle (produção local)

Configure as variáveis de ambiente:

```bash
export DB_URL=jdbc:oracle:thin:@HOST:1521:SID
export DB_USER=seu_usuario
export DB_PASSWORD=sua_senha
```

Depois execute:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
```

---

## 📁 Estrutura do Projeto

```
Challenge_Sprint3_Java/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/futurevet/
│   │   │   │   ├── FutureVetApplication.java  # Classe principal
│   │   │   │   ├── config/       # Carga de dados demo e primeiro admin
│   │   │   │   ├── model/        # Entidades (records)
│   │   │   │   ├── repository/   # Acesso ao banco com JdbcTemplate
│   │   │   │   ├── security/     # Spring Security, JWT e controle de acesso
│   │   │   │   ├── service/      # Regras de negócio
│   │   │   │   └── web/          # Controllers MVC (Thymeleaf)
│   │   │   │       ├── api/      # Controllers REST
│   │   │   │       │   └── dto/  # Objetos de entrada e saída da API
│   │   │   │       └── forms/    # Formulários com Bean Validation
│   │   │   └── db/migration/     # Migrations Java do Flyway (V2, V3)
│   │   └── resources/
│   │       ├── db/migration/     # Migration SQL do Flyway (V1)
│   │       ├── templates/        # Views Thymeleaf (HTML)
│   │       ├── static/           # CSS
│   │       ├── application.properties
│   │       ├── application-demo.properties
│   │       └── application-oracle.properties
│   └── test/java/com/futurevet/  # Testes automatizados
├── database/                     # Script DDL do banco
├── render.yaml                   # Configuração de deploy no Render
├── Dockerfile
└── pom.xml
```

---

## 📦 Build para produção

```bash
mvn clean package -DskipTests
java -jar target/*.jar --spring.profiles.active=oracle
```

---

## 📌 Links Importantes

- 🔗 **Repositório GitHub:** https://github.com/joaocaetano1310/Challenge_Sprint3_Java
- 🌐 **Aplicação em produção:** https://challenge-sprint3-java.onrender.com
- 🎬 **Vídeo demonstrativo:** https://youtu.be/jWtMZDGaDf8

---

<p align="center">
  Desenvolvido com ❤️ para o Challenge FIAP 2026 — 2º Semestre
</p>
