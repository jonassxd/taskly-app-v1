<h1 align="center">
  ⚡ Taskly Automator
</h1>

<p align="center">
  Projeto fullstack desenvolvido para demonstrar visão completa do ciclo de desenvolvimento — <br/>
  do back-end ao front-end — com foco em <strong>qualidade de software e testes automatizados E2E</strong>.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/QA-Cypress_E2E-17202C?style=for-the-badge&logo=cypress&logoColor=white"/>
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/HTML%2FCSS%2FJS-Frontend-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"/>
</p>

---

## 🎯 Por que esse projeto existe?

A maioria dos QAs testa o que outros desenvolveram. Eu quis ir além.

O **Taskly Automator** foi construído do zero — back-end, front-end e testes — para demonstrar que um bom QA não precisa ser um "caixa preta" dentro do time. Entender a stack completa me permite:

- **Reproduzir bugs com mais precisão** — sabendo onde o problema pode estar (banco, API ou interface)
- **Escrever testes mais inteligentes** — usando a API diretamente para preparar cenários, não dependendo só da UI
- **Comunicar melhor com o time** — falando a mesma língua dos devs
- **Encontrar problemas antes** — analisando código e identificando riscos antes de chegar nos testes

> Um QA que entende o sistema completo não é substituído por automação. Ele **cria** a automação.

---

## 🧪 Foco principal — Testes E2E com Cypress

O projeto conta com **8 testes automatizados** cobrindo os fluxos críticos da aplicação:

| Suite | Testes | O que valida |
|---|---|---|
| Cadastro de cliente | 4 | Fluxo completo, validações, limpeza de campos |
| Cadastro de tarefa | 4 | Fluxo completo, validações, status inicial |
| Filtros e busca | 8 | Busca por descrição, cliente, status, collapse automático |
| Ações das tarefas | 6 | Iniciar, concluir, deletar, regras de negócio |
| Comportamento do collapse | 3 | UX e estados visuais da interface |

### Estratégias de teste aplicadas

**Isolamento por suite** — cada suite cria seus próprios dados via API no `before()`, sem depender de estado anterior ou de outros testes.

**Dados únicos com timestamp** — uso de `Date.now()` no nome e email garante que cada execução cria dados novos, sem conflito com execuções anteriores.

**Busca por ID no DOM** — ações como iniciar e concluir buscam o elemento pelo `id` da tarefa retornado pela API, não por posição na lista. Isso evita falsos negativos quando há múltiplos itens similares.

**Criação de dados via API** — cenários de pré-condição (ex: ter uma tarefa para iniciar) são criados via `cy.request()` diretamente na API, tornando os testes mais rápidos e confiáveis.

```javascript
// Exemplo: criando dados de teste via API, não pela UI
function criarTarefaViaApi(clienteId) {
  return cy.request({
    method: 'POST',
    url: `${API_URL}/tarefas`,
    body: { ...tarefa, clienteId },
    headers: { 'Content-Type': 'application/json' }
  }).then(res => res.body.id || res.body.data?.id);
}
```

---

---

## 🧠 Test Design & QA Strategy

Além da automação E2E, o projeto foi estruturado com foco em práticas reais de QA e SDET.

### 🎯 Abordagem de testes

Os testes foram desenhados considerando:

- Uso de dados fixos nos testes para garantir previsibilidade e consistência nas execuções
- Cobertura de fluxos críticos do sistema
- Validação de regras de negócio no front e no back-end
- Testes positivos e negativos
- Script de limpeza geral (clean-all) para remover todos os dados do sistema entre execuções de teste

---

### 🧩 Organização dos testes

Os testes seguem uma estrutura baseada em:

- `describe` → separação por contexto funcional
- `it` → cenários de teste claros e objetivos
- `before / beforeEach` → controle de estado
- Helpers reutilizáveis para criação de dados

---

### 🔗 Uso estratégico da API nos testes

Ao invés de depender exclusivamente da interface:

- Dados são criados via API (`cy.request`)
- Cenários são preparados antes da execução
- A UI é usada apenas para validação do comportamento

Isso torna os testes:

- Mais rápidos
- Mais confiáveis
- Menos frágeis

---

### ⚠️ Validação de regras de negócio

As regras críticas foram testadas em múltiplas camadas:

- Interface (botões desabilitados)
- API (validação de status)
- Domínio (lógica dentro das entidades)

---

### 🔄 Controle de estado

- Cada suíte cria seus próprios dados
- Uso de `timestamp` evita colisões
- Nenhum teste depende de outro

---

### 🎯 Objetivo

Garantir que:

- O sistema funcione de ponta a ponta
- As regras de negócio sejam respeitadas
- O comportamento da UI esteja consistente
- Bugs sejam detectados antes de produção

---

### 🚀 Visão

Este projeto demonstra:

- Capacidade de desenvolver e testar o sistema
- Criação de testes automatizados confiáveis
- Entendimento de arquitetura de software
- Mentalidade orientada à qualidade desde o desenvolvimento



## 🔄 Ciclo de vida da tarefa testado

```
PENDENTE ──► EM_ANDAMENTO ──► CONCLUÍDA
```

Os testes validam não só o caminho feliz, mas também as **regras de negócio**:

- ❌ Não deve concluir uma tarefa PENDENTE
- ❌ Não deve deletar uma tarefa EM_ANDAMENTO
- ❌ Não deve iniciar uma tarefa já CONCLUÍDA
- ✅ Deve bloquear botões na interface conforme o status

---

## 🏗️ Stack completa

### Back-end — API REST
Desenvolvida em **Java com Spring Boot**, seguindo arquitetura em camadas e boas práticas:

- **Controller → Service → Repository** — separação clara de responsabilidades
- **Domain Model** — regras de negócio encapsuladas nas entidades (`Tarefa.iniciar()`, `Tarefa.concluir()`)
- **DTOs** — entidades não expostas diretamente (`ClienteResponse`, `TarefaResponse`)
- **Resposta padronizada** — todos os endpoints retornam `ApiResponse<T>`
- **Tratamento centralizado de erros** — `GlobalExceptionHandler`
- **Validação de campos** — Bean Validation com mensagens descritivas

### Front-end — Interface web
Desenvolvida em **HTML, CSS e JavaScript puro**, sem frameworks:

- Consumo da API REST com `fetch`
- Filtros por status e busca em tempo real
- Collapse de listas com contador dinâmico
- Botões desabilitados conforme regra de negócio
- Layout responsivo com tema escuro

---

## 📡 Endpoints da API

### Clientes

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/clientes` | Cadastrar cliente |
| `GET` | `/clientes` | Listar todos |
| `GET` | `/clientes/{id}` | Buscar por ID |
| `PUT` | `/clientes/{id}` | Atualizar |
| `DELETE` | `/clientes/{id}` | Deletar |

### Tarefas

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/tarefas` | Cadastrar tarefa |
| `GET` | `/tarefas` | Listar todas |
| `GET` | `/tarefas/{id}` | Buscar por ID |
| `GET` | `/tarefas/cliente/{clienteId}` | Tarefas de um cliente |
| `GET` | `/tarefas/status/{status}` | Filtrar por status |
| `GET` | `/tarefas/cliente/{clienteId}/status/{status}` | Filtrar por cliente e status |
| `PUT` | `/tarefas/{id}/iniciar` | Iniciar tarefa |
| `PUT` | `/tarefas/{id}/concluir` | Concluir tarefa |
| `DELETE` | `/tarefas/{id}` | Deletar tarefa |

### Exemplo de resposta padrão

```json
{
  "status": 200,
  "data": {
    "id": 1,
    "descricao": "Criar proposta comercial",
    "data": "2025-12-31",
    "status": "PENDENTE",
    "clienteNome": "João Silva",
    "clienteEmail": "joao@email.com"
  },
  "mensagem": null
}
```

---

## 🗂️ Estrutura do repositório

```
taskly/
├── back-end/
│   └── src/main/java/com/jonas/automator/
│       ├── controller/
│       ├── services/
│       ├── model/
│       ├── repositories/
│       ├── dto/
│       └── exceptions/
│
├── front-end/
│   ├── index.html
│   ├── style.css
│   └── script.js
│
└── cypress/
    └── e2e/
        └── automator.cy.js
```

---

## ⚙️ Como rodar

### Pré-requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Node.js (para o Cypress)
- Live Server ou similar para o front-end

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/taskly-app.git
cd taskly-app
```

### 2. Configurar o banco de dados

```sql
CREATE DATABASE taskly;
```

```bash
export DB_URL=jdbc:postgresql://localhost:5432/taskly
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha
```

### 3. Rodar o back-end

```bash
cd back-end
mvn spring-boot:run
```

API disponível em `http://localhost:8080`

### 4. Rodar o front-end

Abra o `front-end/index.html` com Live Server ou qualquer servidor HTTP local.

### 5. Rodar os testes

```bash
npx cypress open   # interface visual
npx cypress run    # linha de comando
```

---

## 👨‍💻 Autor

Desenvolvido por **Jonas** — QA Engineer com visão fullstack e foco em automação de testes e qualidade de software.

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/jonassxd)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/jonas-eduardo-santos-da-silva-6930a8234/)
