// cypress/e2e/clean-all.cy.js
const BASE_URL = 'http://127.0.0.1:5500/frontend/index.html';
const API_URL = 'http://localhost:8080';

// ─────────────────────────────────────────
// NORMALIZADOR (resolve seu erro)
// ─────────────────────────────────────────

function extrairArray(resBody) {
  if (Array.isArray(resBody)) return resBody;
  if (Array.isArray(resBody?.data)) return resBody.data;
  if (Array.isArray(resBody?.tarefas)) return resBody.tarefas;
  if (Array.isArray(resBody?.clientes)) return resBody.clientes;
  return [];
}

// ─────────────────────────────────────────
// DELETE TAREFAS
// ─────────────────────────────────────────

function deletarTodasTarefas() {
  return cy.request(`${API_URL}/tarefas`).then(res => {

    const tarefas = extrairArray(res.body);

    tarefas.forEach(t => {
      if (t?.id) {
        cy.request('DELETE', `${API_URL}/tarefas/${t.id}`);
      }
    });

  });
}

// ─────────────────────────────────────────
// DELETE CLIENTES
// ─────────────────────────────────────────

function deletarTodosClientes() {
  return cy.request(`${API_URL}/clientes`).then(res => {

    const clientes = extrairArray(res.body);

    clientes.forEach(c => {
      if (c?.id) {
        cy.request('DELETE', `${API_URL}/clientes/${c.id}`);
      }
    });

  });
}

// ─────────────────────────────────────────
// TESTE
// ─────────────────────────────────────────

describe('Clean All - Remove tudo do sistema', () => {

  it('deve deletar todas as tarefas e clientes', () => {

    deletarTodasTarefas();
    deletarTodosClientes();

  });

});