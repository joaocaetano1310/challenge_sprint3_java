# FutureVet — Java Advanced · Sprint 3

Aplicação web nova em Java 17/Spring Boot, baseada no esquema do arquivo `FutureVetDB.sql` enviado em 08/09/2026. Reúne tutores, animais, agenda e carteira de vacinação. O objetivo é reduzir desencontros na agenda e facilitar o acompanhamento de prevenção dos pets.

## 👥 Equipe

| Nome | RM |
|---|---|
| João Victor Caetano Alves da Silva | 562074 |
| João Victor Bueno Castelini da Silva | 564115 |
| Ryan Vetoriano | 565667 |
| Felipe Furlanetto | 562766 |
| Raul Rezende Iemini Aguiar | 564002 |

## Comece aqui

Repositório: [Challenge_Sprint3_Java](https://github.com/joaocaetano1310/Challenge_Sprint3_Java).

Para obter o código pelo Git:

```sh
git clone https://github.com/joaocaetano1310/Challenge_Sprint3_Java.git
cd Challenge_Sprint3_Java
```

Execute os comandos Maven na pasta que contém `pom.xml`. Se o projeto estiver dentro de uma subpasta `FutureVet`, entre nela primeiro.

Para conhecer a aplicação **sem conectar ao Oracle**, extraia o ZIP inteiro e execute `iniciar-demo.cmd` no Windows. É necessário Java 17 ou 21 instalado. Mantenha a pasta `executavel/lib` junto do JAR.

Acesse **http://localhost:8080**. Se a porta estiver ocupada, use `java -jar executavel/futurevet.jar --spring.profiles.active=demo --server.port=8086` e abra a porta correspondente. Pare com Ctrl+C no terminal.

| Perfil local | E-mail | Senha de demonstração |
|---|---|---|
| Tutor | tutor@futurevet.local | Futurevet123! |
| Clínica / ADMIN | clinica@futurevet.local | Futurevet123! |
| Outro tutor, sem animais | outro@futurevet.local | Futurevet123! |

Essas contas são fictícias, exclusivas do perfil demo. O H2 em memória serve apenas para demonstração e testes: **os dados desaparecem ao encerrar a aplicação**. Não use o perfil demo como entrega de banco Oracle nem para publicação online. O modo Oracle não cria essas contas.

## Abrir no Eclipse

1. File → Import → Maven → Existing Maven Projects.
2. Escolha a pasta FutureVet que contém `pom.xml`.
3. Selecione Java 17 ou 21 em Installed JREs; o projeto compila para Java 17.
4. Aguarde o Maven baixar as dependências. Use Maven → Update Project se necessário.
5. Execute `com.futurevet.FutureVetApplication` como Java Application. Sem perfil explícito, inicia o demo local.

Não importe a pasta `executavel` como outro projeto. Não há um subprojeto `demo/`, nem é necessário Lombok.

## Compilar pelo Maven

Requisitos: JDK 17+, Maven 3.9+ e acesso ao Maven Central no primeiro build.

```text
mvn clean verify
mvn spring-boot:run
```

O build padrão produz `target/futurevet-1.0.0.jar`, executável com `java -jar target/futurevet-1.0.0.jar`. O JAR fornecido em `executavel/` é uma distribuição alternativa, com bibliotecas ao lado, preparada para testar sem baixar dependências. Consulte `docs/VALIDACAO.md` sobre a validação efetivamente realizada.

## Banco Oracle

**Leia `docs/ORACLE.md` antes de usar a conexão da FIAP.** O novo projeto precisa de extensões pequenas no esquema para cumprir os dois perfis e o fluxo de atendimento. O script original sozinho não contém essas extensões.

O perfil `oracle` usa DB_URL, DB_USER e DB_PASSWORD, sem credenciais embutidas. As migrations nesse perfil vêm desativadas até que DB_MIGRATIONS_ENABLED seja configurada explicitamente. A aplicação não funcionará contra o banco original sem a atualização descrita no guia.

## Requisitos do PDF — páginas 20 a 22

| Critério de Java Advanced, Sprint 3 | Implementação |
|---|---|
| Frontend (30 pontos) | Thymeleaf, CSS responsivo, login, cadastro, painel, animais, consultas, vacinas e área da clínica |
| Flyway (20 pontos) | V1 SQL com esquema base; V2 Java com extensões/sequences; V3 Java com proteção das senhas |
| Security (30 pontos) | TUTOR e ADMIN, sessão, BCrypt, CSRF, rotas administrativas e verificação do proprietário no serviço |
| Dois fluxos além do CRUD (20 pontos) | Agendamento/confirmar/reagendar/cancelar/concluir; classificação de carteira/registro de reforço/próxima dose/histórico |

O escopo implementado é Java Advanced, Sprint 3. O PDF reúne outras disciplinas e a Sprint 4: não há promessa de completar automaticamente Azure, mobile, MongoDB, IA, vídeos ou os novos procedimentos exigidos na disciplina de banco. O projeto web novo não expõe uma API REST compatível com o aplicativo mobile anterior.

## Como usar

**Tutor:** cadastra conta e animais; consulta os próprios dados; agenda e reagenda consultas; cancela informando motivo; registra vacinas e reforços. Nunca pode escolher um perfil administrativo no cadastro público nem acessar registros de outro tutor por ID.

**Clínica:** acessa a agenda e os registros de todos os tutores, confirma solicitações e conclui atendimentos após o horário marcado. Consulta a lista de tutores/equipe. A criação de animal continua vinculada à conta atual; a clínica pode editar animais já cadastrados pelos tutores.

### Fluxo de consulta

Solicitação futura → validação do horário e conflito → AGENDADA → confirmação pela clínica → CONFIRMADA → atendimento após a data/hora → REALIZADA. Tutor ou clínica podem cancelar consultas ativas informando motivo. Reagendar retorna à espera de confirmação. Horários: segunda a sábado, 08:00–18:00. O mesmo animal ou mesmo local não pode ocupar duas consultas ativas no mesmo instante. Use o mesmo nome de local/consultório para que a regra identifique o recurso.

O bloqueio de uma linha dedicada serializa gravações de agenda dentro da transação, inclusive entre instâncias da aplicação. Consultas legadas ficam INDEFINIDA: a aplicação não presume que foram realizadas ou canceladas. Reagende para ingressar no fluxo novo. Cancelamento preserva o registro e libera o horário; não é DELETE.

### Fluxo de vacina

Registrar aplicação → classificar pela próxima data (atrasada, próxima em até 30 dias, em dia ou sem previsão) → registrar reforço com intervalo orientado pelo veterinário → calcular próxima dose → preservar dose anterior como histórico. Não é feita recomendação médica automática de intervalo. A dose original aceita um único reforço; o reforço pode receber o próximo, formando uma cadeia. Datas e propriedade são validadas, com trava transacional e constraint única.

Doses vinculadas a reforços não podem ser editadas/excluídas. Um animal com qualquer histórico não pode ser excluído. As restrições preservam rastreabilidade. O CRUD completo está disponível em animais sem histórico e em registros de vacina ainda sem vínculos de reforço.

## Organização para estudar e explicar

- `model`: records imutáveis representando os dados.
- `repository`: SQL parametrizado com os nomes reais do Oracle; Spring JDBC.
- `service`: regras e transações, com Clock injetado para datas.
- `security`: login e autorização por perfil/proprietário.
- `web` e `web/forms`: rotas MVC, formulários e validações.
- `templates` e `static/css`: apresentação.
- `db/migration`: versionamento Flyway; Java migrations em `src/main/java/db/migration`.
- `src/test`: testes de integração, segurança, fluxos e migração.

O PDF não obriga JPA/Hibernate; foi escolhido JDBC para explicitar o SQL e evitar nomes inferidos incompatíveis com o esquema legado. As relações são carregadas nos SELECTs, sem proxies LAZY.

## Entrega e avaliação oral

O PDF pede repositório público com README e vídeo da aplicação funcionando, máximo de 10 minutos. Não publique credenciais da FIAP. O roteiro está em `docs/ROTEIRO-VIDEO.md`. O aluno deve entender e explicar o código, as regras e a utilização de IA; leia `docs/ARQUITETURA.md` e execute os cenários. Publicação GitHub, vídeo e deploy não foram realizados por esta entrega de arquivos.

Referências técnicas: [Spring Security — formulário e CSRF](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html), [Flyway — Java migrations](https://documentation.red-gate.com/flyway/flyway-concepts/migrations/java-based-migrations).
