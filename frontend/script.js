/* ESTADO */
let todasTarefas = [];
let filtroStatus = 'todos';
let todosClientes = [];

/* MODAL */
function abrirModal(texto) {
  document.getElementById("modalText").innerText = texto;
  document.getElementById("modal").classList.remove("hidden");
}

function fecharModal() {
  document.getElementById("modal").classList.add("hidden");
  document.querySelector(".modal-actions").innerHTML = `<button onclick="fecharModal()">OK</button>`;
}

/* CLIENTES */
async function carregarClientes() {
  try {
    const res = await fetch('http://localhost:8080/clientes');
    const data = await res.json();
    todosClientes = data.data || data;

    const select = document.getElementById('clienteId');
    select.innerHTML = '<option value="">Selecione um cliente</option>';
    todosClientes.forEach(c => {
      const opt = document.createElement('option');
      opt.value = c.id;
      opt.textContent = c.nome;
      select.appendChild(opt);
    });

    renderClientes(todosClientes);
  } catch(e) {
    console.error(e);
  }
}

function filtrarClientes() {
  const busca = document.getElementById('buscaNome').value.toLowerCase();
  const filtrados = todosClientes.filter(c => c.nome.toLowerCase().includes(busca));
  const lista = document.getElementById('listaClientes');
  const icon = document.getElementById('iconClientes');
  if (busca.length > 0) {
    lista.classList.remove('collapsed');
    document.getElementById('buscaClientesWrap').classList.remove('hidden');
    icon.textContent = '▲';
  }
  renderClientes(filtrados);
}

function renderClientes(clientes) {
  const lista = document.getElementById('listaClientes');
  const contador = document.getElementById('contadorClientes');
  lista.innerHTML = '';
  contador.textContent = `(${clientes.length})`;

  if (clientes.length === 0) {
    lista.innerHTML = '<li class="vazio">Nenhum cliente encontrado.</li>';
    return;
  }

  clientes.forEach(c => {
    lista.innerHTML += `
      <li>
        <span class="campo-label">Nome</span><br>
        <strong>${c.nome}</strong><br><br>
        <span class="campo-label">Email</span><br>
        ${c.email}<br><br>
        <span class="campo-label">Telefone</span><br>
        ${c.telefone}
      </li>`;
  });
}

async function criarCliente() {
  const nome = document.getElementById('nome').value.trim();
  const email = document.getElementById('email').value.trim();
  const telefone = document.getElementById('telefone').value.trim();

  if (!nome || !email || !telefone) { abrirModal("Preencha todos os campos."); return; }

  try {
    const res = await fetch('http://localhost:8080/clientes', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nome, email, telefone })
    });

    if (res.status === 201) {
      abrirModal("Cliente criado com sucesso!");
      document.getElementById('nome').value = '';
      document.getElementById('email').value = '';
      document.getElementById('telefone').value = '';
      carregarClientes();
    } else abrirModal("Erro ao criar cliente.");
  } catch (e) {
    console.error(e);
    abrirModal("Erro na requisição.");
  }
}

/* TOGGLE */
function toggleClientes() {
  const lista = document.getElementById('listaClientes');
  const icon = document.getElementById('iconClientes');
  const buscaWrap = document.getElementById('buscaClientesWrap');
  const aberto = !lista.classList.contains('collapsed');
  lista.classList.toggle('collapsed', aberto);
  buscaWrap.classList.toggle('hidden', aberto);
  icon.textContent = aberto ? '▼' : '▲';
}

function toggleTarefas() {
  const lista = document.getElementById('listaTarefas');
  const icon = document.getElementById('iconTarefas');
  const aberto = !lista.classList.contains('collapsed');
  lista.classList.toggle('collapsed', aberto);
  icon.textContent = aberto ? '▼' : '▲';
}

/* TAREFAS */
async function carregarTarefas() {
  try {
    const res = await fetch('http://localhost:8080/tarefas');
    const data = await res.json();
    todasTarefas = data.data || data;
    filtrarTarefas();
  } catch (e) { console.error(e); }
}

function setFiltro(status, event) {
  filtroStatus = status;
  document.querySelectorAll('.filtro').forEach(btn => btn.classList.remove('ativo'));
  event.target.classList.add('ativo');
  filtrarTarefas();
}

function filtrarTarefas() {
  const busca = (document.getElementById('buscaCliente').value || '').toLowerCase();
  const filtradas = todasTarefas.filter(t => {
    const matchStatus = filtroStatus === 'todos' || t.status === filtroStatus;
    const matchCliente = t.clienteNome?.toLowerCase().includes(busca) ||
                         t.clienteEmail?.toLowerCase().includes(busca) ||
                         t.descricao?.toLowerCase().includes(busca);
    return matchStatus && matchCliente;
  });

  const lista = document.getElementById('listaTarefas');
  const icon = document.getElementById('iconTarefas');
  if (busca.length > 0 || filtroStatus !== 'todos') lista.classList.remove('collapsed');
  renderTarefas(filtradas);
}

function renderTarefas(tarefas) {
  const lista = document.getElementById('listaTarefas');
  const contador = document.getElementById('contadorTarefas');
  lista.innerHTML = '';
  contador.textContent = `(${tarefas.length})`;

  if (!tarefas.length) {
    lista.innerHTML = '<li class="vazio">Nenhuma tarefa encontrada.</li>';
    return;
  }

  tarefas.forEach(t => {
    const statusClasse = {
      'PENDENTE': 'badge-pendente',
      'EM_ANDAMENTO': 'badge-andamento',
      'CONCLUIDA': 'badge-concluida'
    }[t.status] || '';

    const podeIniciar = t.status === 'PENDENTE';
    const podeConcluir = t.status === 'EM_ANDAMENTO';
    const podeDeletar = t.status !== 'EM_ANDAMENTO';

    lista.innerHTML += `
      <li>
        <span class="campo-label">Descrição</span><br>
        <strong>${t.descricao}</strong><br><br>
        <span class="campo-label">Cliente</span><br>
        ${t.clienteNome} — ${t.clienteEmail}<br><br>
        <span class="badge ${statusClasse}">${t.status}</span>

        <div class="actions">
          <button class="iniciar" onclick="iniciar(${t.id})" ${!podeIniciar ? 'disabled' : ''}>Iniciar</button>
          <button class="concluir" onclick="concluir(${t.id})" ${!podeConcluir ? 'disabled' : ''}>Concluir</button>
          <button class="deletar" onclick="confirmarDeletar(${t.id})" ${!podeDeletar ? 'disabled' : ''}>Deletar</button>
        </div>
      </li>`;
  });
}

async function criarTarefa() {
  const descricao = document.getElementById('descricao').value.trim();
  const data = document.getElementById('data').value;
  const clienteId = document.getElementById('clienteId').value;

  if (!descricao || !data || !clienteId) { abrirModal("Preencha todos os campos da tarefa."); return; }

  try {
    const res = await fetch('http://localhost:8080/tarefas', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ descricao, data, clienteId: parseInt(clienteId) })
    });

    if (res.status === 201) {
      abrirModal("Tarefa criada!");
      document.getElementById('descricao').value = '';
      document.getElementById('data').value = '';
      document.getElementById('clienteId').value = '';
      carregarTarefas();
    } else abrirModal("Erro ao criar tarefa.");
  } catch (e) {
    console.error(e);
    abrirModal("Erro na requisição.");
  }
}

async function iniciar(id) {
  try { await fetch(`http://localhost:8080/tarefas/${id}/iniciar`, { method: 'PUT' }); carregarTarefas(); }
  catch(e){ abrirModal("Erro ao iniciar tarefa."); }
}

async function concluir(id) {
  try {
    const res = await fetch(`http://localhost:8080/tarefas/${id}/concluir`, { method: 'PUT' });
    const data = await res.json();
    if (res.status !== 200) abrirModal(data.mensagem);
    else { abrirModal("Tarefa concluída!"); carregarTarefas(); }
  } catch(e) { abrirModal("Erro ao concluir tarefa."); }
}

function confirmarDeletar(id) {
  abrirModal("Tem certeza que deseja deletar esta tarefa?");
  const actions = document.querySelector(".modal-actions");
  actions.innerHTML = `
    <button onclick="deletarTarefa(${id})">Confirmar</button>
    <button class="btn-cancelar" onclick="fecharModal()">Cancelar</button>
  `;
}

async function deletarTarefa(id) {
  try { fecharModal(); await fetch(`http://localhost:8080/tarefas/${id}`, { method: 'DELETE' }); abrirModal("Tarefa deletada!"); carregarTarefas(); }
  catch(e){ abrirModal("Erro ao deletar tarefa."); }
}

/* INIT */
carregarClientes();
carregarTarefas();