# Validação realizada em 08/09/2026

## Resultado

- Código principal e testes compilados para Java 17 pelo compilador Eclipse JDT/ECJ 3.45.0. Compilação final sem avisos.
- 20 testes JUnit aprovados; nenhum teste ignorado, abortado ou reprovado na execução final.
- Spring Boot 3.2.5 iniciado com H2 2.2.224 em modo Oracle e migrations Flyway 9.22.3.
- Distribuição final `executavel/futurevet.jar` iniciada com Java 17.0.18, com bibliotecas da pasta `lib`.
- Navegador: login de tutor, login administrativo na distribuição final, painel, carteira de vacinação e confirmação de consulta pela clínica. Inspeção visual do painel em desktop e largura de celular de 390px.
- As telas de formulários, listagens e área administrativa também foram renderizadas nos testes MockMvc.
- Nenhuma conexão, migration ou alteração foi executada no Oracle da FIAP.

## Testes

1. Login com senha correta e incorreta.
2. Proteção da área privada e rejeição de POST sem CSRF.
3. Renderização das telas do tutor.
4. Renderização das telas da clínica.
5. Renderização das telas públicas.
6. Bloqueio de leitura/escrita em registros de outro tutor e área administrativa.
7. Conflito de agenda e liberação do horário após cancelamento.
8. Tutor não confirma nem conclui atendimento.
9. Clínica confirma; conclusão exige horário passado e situação correta.
10. Reagendamento volta a aguardar confirmação.
11. Recusa de data passada, domingo e horário com segundos.
12. Reforço calcula próxima data, preserva histórico e não pode ser repetido para a mesma dose.
13. Dose sem próxima data não inventa prazo/situação em dia.
14. CRUD de animal com idade textual, peso decimal e bloqueio de exclusão de histórico.
15. Erros de formulário mantêm a página com os campos inválidos identificados.
16. Cadastro normaliza CPF/telefone, grava hash e ignora tentativa de criar ADMIN por parâmetro.
17. CRUD de vacina com campos opcionais.
18. Duas reservas simultâneas: exatamente uma é aceita para o mesmo horário.
19. Migração de legado preserva dados, cria sequences acima do máximo, mantém status desconhecido e protege a senha; nova execução não reaplica migrations concluídas.
20. Horário legado inválido impede a atualização antes de converter senhas.

## Limitações da evidência

O Maven conseguiu resolver as dependências já disponíveis, mas o compilador javac apresentou AccessDeniedException no sistema de arquivos desta sessão. O download dos componentes ausentes do Surefire também foi bloqueado pela rede do ambiente. Por isso **não há alegação de `mvn clean verify` aprovado aqui**.

Para concluir a validação local, o mesmo código-fonte foi compilado com ECJ já instalado no Eclipse; os testes foram executados diretamente pelo JUnit Platform 1.13.4/Jupiter 5.13.4 disponíveis. O pom mantém os testes padrão do Spring Boot, executáveis pelo Maven em ambiente com acesso às dependências. A distribuição pronta foi testada separadamente com Java 17 no navegador.

Flyway avisa que a versão local de H2 é mais nova que a última versão H2 oficialmente testada por essa edição do Flyway. As migrations e os testes passaram. H2 em modo Oracle não reproduz todos os comportamentos Oracle: em especial DDL com commits implícitos, permissões, configurações NLS e concorrência devem ser validados na cópia do banco Oracle antes da adoção oficial.

O teste de horário inválido provoca uma falha de migration de propósito para confirmar o bloqueio; essa falha esperada é tratada pela asserção. Não representa migration pendente no banco de demonstração.

## Antes de entregar ao professor

Execute o build Maven no Eclipse/terminal com acesso às dependências, valide a atualização em uma cópia Oracle, confira dados reais com nulos, teste login de conta legada e os dois perfis/fluxos. Grave o vídeo e publique o repositório apenas depois dessa validação. Use `ORACLE.md` e `ROTEIRO-VIDEO.md`.
