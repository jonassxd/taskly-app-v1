// cypress/e2e/automator.cy.js

const BASE_URL = 'http://127.0.0.1:5500/frontend/index.html';
const API_URL  = 'http://localhost:8080';

// ─────────────────────────────────────────
// DADOS REAIS (SEM TIMESTAMP)
// ─────────────────────────────────────────

const cliente = {
  nome: 'João Silva',
  email: 'joao.silva@email.com',
  telefone: '11999999999'
};

const tarefa = {
  descricao: 'Revisar documentação do projeto',
  data: '2025-12-31'
};

// ─────────────────────────────────────────
// HELPERS
// ─────────────────────────────────────────

function criarClienteViaApi() {
  return cy.request({
    method: 'POST',
    url: `${API_URL}/clientes`,
    body: cliente,
    headers: { 'Content-Type': 'application/json' }
  }).then(res => res.body.id || res.body.data?.id);
}

function criarTarefaViaApi(clienteId) {
  return cy.request({
    method: 'POST',
    url: `${API_URL}/tarefas`,
    body: { ...tarefa, clienteId },
    headers: { 'Content-Type': 'application/json' }
  }).then(res => res.body.id || res.body.data?.id);
}

function abrirTarefas() {
  cy.get('#listaTarefas').then($lista => {
    if ($lista.hasClass('collapsed')) {
      cy.contains('.collapse-header', 'Tarefas').click();
    }
  });
}

function abrirClientes() {
  cy.get('#listaClientes').then($lista => {
    if ($lista.hasClass('collapsed')) {
      cy.contains('.collapse-header', 'Clientes').click();
    }
  });
}

// ─────────────────────────────────────────
// BUSCAS
// ─────────────────────────────────────────

function getTarefaItem() {
  return cy.get('#listaTarefas li')
    .filter(`:contains("${tarefa.descricao}")`)
    .first();
}

function getTarefaById(id) {
  return cy.get('#listaTarefas li')
    .filter(`:has(.iniciar[onclick="iniciar(${id})"])`);
}

// ─────────────────────────────────────────
// SUITE PRINCIPAL
// ─────────────────────────────────────────

describe('Automator — testes E2E', () => {

  // ─────────────────────────────────────────
  // CLIENTE
  // ─────────────────────────────────────────

  describe('Cadastro de cliente', () => {

    beforeEach(() => cy.visit(BASE_URL));

    it('deve cadastrar um cliente com sucesso', () => {
      cy.get('#nome').type(cliente.nome);
      cy.get('#email').type(cliente.email);
      cy.get('#telefone').type(cliente.telefone);
      cy.contains('button', 'Cadastrar Cliente').click();

      cy.get('#modalText').should('contain', 'Cliente criado com sucesso');
      cy.contains('button', 'OK').click();

      abrirClientes();
      cy.get('#listaClientes').should('contain', cliente.nome);
    });

  });

  // ─────────────────────────────────────────
  // TAREFA
  // ─────────────────────────────────────────

  describe('Cadastro de tarefa', () => {

    let clienteId;

    before(() => {
      criarClienteViaApi().then(id => { clienteId = id; });
    });

    beforeEach(() => cy.visit(BASE_URL));

    it('deve cadastrar uma tarefa com sucesso', () => {
      cy.get('#descricao').type(tarefa.descricao);
      cy.get('#data').type(tarefa.data);
      cy.get('#clienteId').select(`${clienteId}`);

      cy.contains('button', 'Cadastrar Tarefa').click();
      cy.get('#modalText').should('contain', 'Tarefa criada');
    });

  });

  // ─────────────────────────────────────────
  // AÇÕES
  // ─────────────────────────────────────────

  describe('Ações das tarefas', () => {

    let clienteId;
    let tarefaId;

    before(() => {
      criarClienteViaApi().then(id => { clienteId = id; });
    });

    beforeEach(() => {
      criarTarefaViaApi(clienteId).then(id => {
        tarefaId = id;

        cy.visit(BASE_URL);
        abrirTarefas();
      });
    });

    it('deve iniciar tarefa', () => {

      cy.intercept('PUT', '**/tarefas/**').as('updateTarefa');

      getTarefaById(tarefaId).within(() => {
        cy.get('.iniciar').click();
      });

      cy.wait('@updateTarefa');

      getTarefaById(tarefaId).within(() => {
        cy.get('.badge')
          .should('have.class', 'badge-andamento');
      });

    });

    it('deve concluir tarefa', () => {

      cy.intercept('PUT', '**/tarefas/**').as('updateTarefa');

      getTarefaById(tarefaId).within(() => {
        cy.get('.iniciar').click();
      });

      cy.wait('@updateTarefa');

      getTarefaById(tarefaId).within(() => {
        cy.get('.concluir').click();
      });

      cy.wait('@updateTarefa');

      cy.get('#modalText').should('contain', 'Tarefa concluída');

    });

  });

});