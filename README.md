# Desafio Itaú — Backend (PT-BR)

Projeto simples em Spring Boot que recebe transações em memória e expõe estatísticas calculadas sobre os últimos N segundos.

Este README traz instruções de execução, testes, configuração segura e empacotamento (Docker).

## Destaques

- Linguagem: Java 21
- Framework: Spring Boot 3.5
- Armazenamento: somente em memória (sem banco de dados)
- Janela padrão: `60` segundos (configurável via `statistics.window-seconds`)

---

## Pré-requisitos

- JDK 21 instalado e disponível em `PATH`.
- PowerShell (Windows) ou terminal compatível para executar o Maven Wrapper (`mvnw`).
- (Opcional) Docker para criar a imagem da aplicação.

## Executando a aplicação

Na raiz do projeto:

```powershell
./mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

## Endpoints

- POST `/transacao`

  - Corpo (JSON):

    ```json
    {
      "valor": 123.45,
      "dataHora": "2024-04-14T21:23:00.789-03:00"
    }
    ```

  - Respostas:
    - `201 Created` — transação aceita (inclui `valor == 0`).
    - `422 Unprocessable Entity` — valores inválidos (ex.: valor negativo, data no futuro).
    - `400 Bad Request` — JSON inválido / erro de parsing.

- DELETE `/transacao`

  - Limpa todas as transações em memória. Retorna `200 OK`.

- GET `/estatistica`
  - Retorna as estatísticas (count, sum, avg, min, max) das transações ocorridas na janela configurada.
  - Se não houver transações na janela, todos os campos retornam `0`.

## Exemplo rápido (curl)

```powershell
curl -X POST http://localhost:8080/transacao -H "Content-Type: application/json" -d '{"valor":100.0,"dataHora":"2024-04-14T21:23:00.789-03:00"}'
curl http://localhost:8080/estatistica
```

## Configuração segura (segredos)

- Mantenha segredos fora do controle de versão. Copie `application-local.properties.example` para `application-local.properties` e preencha credenciais locais.
- Alternativamente, use variáveis de ambiente (ex.: `SPRING_DATASOURCE_PASSWORD` ou `STATISTICS_WINDOW_SECONDS`).

## Testes

Execute a suíte com:

```powershell
./mvnw.cmd test
```

Testes incluídos:

- `TransactionServiceTest` — valida a janela temporal e agregações.
- `TransactionControllerTest` — testes de integração com `MockMvc` e `Clock` fixo.

## Observabilidade e logs

- Actuator disponível em `/actuator/health` e `/actuator/info` (configurado em `application.properties`).
- Logs informam quando transações são aceitas, rejeitadas e quando a fila é limpa. Ajuste níveis em `application.properties`.

## Configurações importantes

- `statistics.window-seconds` (padrão `60`): janela em segundos para calcular as estatísticas.
  - Pode ser sobrescrita por `application-local.properties` ou pela variável `STATISTICS_WINDOW_SECONDS`.

## Docker

Build e execução:

```powershell
docker build -t desafio-itau .
docker run --rm -p 8080:8080 desafio-itau
```

O `Dockerfile` usa build multi-stage e Java 21; passe variáveis via `-e` se precisar sobrescrever propriedades.

## Estrutura simplificada

```text
.
├── pom.xml
├── src/
│   ├── main/java/desafio/itau/springboot
│   └── main/resources
├── postman/
└── application-local.properties.example
```

---

## Dicas para enviar ao GitHub (requisitos do desafio)

- Crie um repositório público e faça commits claros: sugerimos pelo menos 1 commit por endpoint (mínimo de 3 commits).
- Não faça fork de projetos existentes.
- Remova arquivos gerados localmente antes de publicar: `./mvnw.cmd clean`.

Se quiser, eu posso: inicializar um repositório `git`, gerar uma sequência sugerida de commits (um por endpoint) e deixar tudo pronto para enviar ao GitHub.

---

Se desejar alguma seção visual adicional (badges, GIF curto demonstrando a API, ou uma coleção OpenAPI/Swagger), me diga e eu acrescento.
