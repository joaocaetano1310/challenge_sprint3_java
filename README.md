# 🐾 FutureVet — Plataforma de Gestão Veterinária

> Aplicação web desenvolvida com **Spring Boot** como parte do Challenge FIAP 2026 (2º Semestre) em parceria com a **Clyvo Vet**, com foco em saúde contínua e bem-estar animal.

---

## 📋 Sobre o Projeto

O **FutureVet** é uma plataforma web que permite a clínicas veterinárias e tutores gerenciarem de forma centralizada o histórico de saúde dos pets, agendamento de consultas e controle de vacinas. O sistema conta com autenticação segura e controle de acesso baseado em perfis de usuário (ADMIN e USER).

### Funcionalidades principais

- ✅ Cadastro e gerenciamento de tutores e pets
- ✅ Agendamento e histórico de consultas veterinárias
- ✅ Controle de vacinação
- ✅ Autenticação com Spring Security (dois perfis: ADMIN e USER)
- ✅ Controle de versão do banco de dados com Flyway
- ✅ Interface web responsiva com Thymeleaf

---

## 🎬 Vídeo Demonstrativo

[![Assistir no YouTube](https://img.shields.io/badge/YouTube-Assistir%20Demo-red?style=for-the-badge&logo=youtube)](SEU_LINK_DO_YOUTUBE_AQUI)

> Demonstração completa das funcionalidades da aplicação web (máx. 10 min.)

---

## 🚀 Deploy

[![Acessar Aplicação](https://img.shields.io/badge/Render-Aplicação%20Online-46E3B7?style=for-the-badge&logo=render)](SEU_LINK_DO_RENDER_AQUI)

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
| Spring Boot | 3.x | Framework web |
| Spring Security | 3.x | Autenticação e autorização |
| Spring Data JPA | 3.x | Persistência de dados |
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

---

## 🔐 Credenciais de Acesso

| Perfil | E-mail | Senha |
|--------|--------|-------|
| **ADMIN** | admin@futurevet.com | admin123 |
| **USER** | user@futurevet.com | user123 |

> O perfil **ADMIN** tem acesso completo ao sistema, incluindo gerenciamento de usuários. O perfil **USER** tem acesso às funcionalidades padrão de tutores e pets.

---

## 🗄️ Banco de Dados

O projeto utiliza **Flyway** para controle de versão do banco. As migrations são executadas automaticamente na inicialização.

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
│   │   ├── java/com/futurevet/
│   │   │   ├── controller/      # Controllers MVC (rotas e páginas)
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositórios Spring Data
│   │   │   ├── security/        # Configuração Spring Security
│   │   │   └── service/         # Regras de negócio
│   │   └── resources/
│   │       ├── db/migration/    # Scripts Flyway (V1__, V2__...)
│   │       ├── templates/       # Views Thymeleaf (HTML)
│   │       ├── static/          # CSS, JS, imagens
│   │       ├── application.properties
│   │       ├── application-demo.properties
│   │       └── application-oracle.properties
├── render.yaml                  # Configuração de deploy no Render
├── Dockerfile
└── pom.xml
```

---

## 📄 Documentação da API

Com a aplicação rodando, acesse o Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
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
- 🌐 **Aplicação em produção:** SEU_LINK_DO_RENDER_AQUI
- 🎬 **Vídeo demonstrativo:** SEU_LINK_DO_YOUTUBE_AQUI

---

<p align="center">
  Desenvolvido com ❤️ para o Challenge FIAP 2026 — 2º Semestre
</p>
