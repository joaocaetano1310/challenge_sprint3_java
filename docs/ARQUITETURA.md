# Guia para entender e explicar o projeto

O navegador envia um formulário com token CSRF. Spring Security verifica sessão e permissão de rota. O controller valida o formulário e chama o serviço. O serviço verifica o proprietário e aplica regras dentro de uma transação. O repository executa SQL com parâmetros e devolve records. Thymeleaf renderiza HTML com escape de texto.

Foi escolhido Spring JDBC, porque o esquema Oracle legado tem nomes e tipos bem definidos; o PDF não exige JPA. Não há concatenação de valores vindos do usuário em SQL. A única composição de identificador é a seleção interna de sequence em Ids, limitada a quatro nomes constantes.

## Pontos para a avaliação oral

1. Explique por que `usuarioId` não vem do formulário de cadastro de animal: ele vem da sessão e impede criar dados em nome de outro tutor.
2. Mostre `Acesso.dono`: conhecer um ID não é permissão para acessar aquele registro.
3. Mostre `AgendaService.agendar` e a transação com `bloquearAgenda`: sem a trava, dois pedidos simultâneos poderiam passar pela consulta de conflito antes de gravar.
4. Explique a diferença entre agendar, confirmar e concluir. O tutor não pode confirmar/concluir; quem reagenda volta a aguardar confirmação.
5. Mostre `CarteiraService.classificar`: NULL significa informação ausente, e não vacina em dia. O intervalo do reforço é informado conforme orientação veterinária, não recomendado pelo software.
6. Explique `ID_DOSE_ORIGEM`: mantém a cadeia e a constraint única bloqueia dois reforços para a mesma dose.
7. Mostre as migrations: V1 representa o banco original; V2 amplia o modelo; V3 converte senhas sem enviar dados a serviços externos. A conversão acontece na aplicação, com BCrypt, e não com um truncamento SQL.
8. Explique o baseline 1 apenas para um schema legado já existente e o motivo de não apagar o histórico anterior.
9. Explique a restrição de exclusão de animais com histórico e de doses encadeadas.
10. Leia os testes e justifique quais erros cada um previne. Os testes locais usam H2 em modo Oracle, que não substitui testar o Oracle real.

O código foi elaborado com auxílio de IA. Antes da avaliação, execute, leia e explique com suas palavras cada fluxo; a existência dos arquivos não substitui o entendimento individual exigido no enunciado.
