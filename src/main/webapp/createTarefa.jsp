<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<jsp:useBean id="statusList" scope="request" type="java.util.List<java.lang.String>"/>

<dialog id="create-tarefa-modal">
<h2>Adicionar tarefa</h2>
<form action="tarefas" method="post">
    <label for="titulo">Título</label>
    <input id="titulo" name="titulo" type="text" placeholder="Titulo">
    <label for="dataExecucao">Data execução</label>
    <input id="dataExecucao" name="dataExecucao" type="date" placeholder="Data execução">
    <label for="status">Status</label>
    <select id="status" name="status">
        <c:forEach var="status" items="${statusList}">
            <option >${status}</option>
        </c:forEach>
    </select>

    <div id="modal-actions-buttons-box">
        <button onclick="createTarefaModal.close(); return false;" class="medium-button secondary">Cancelar</button>
        <button type="submit" class="medium-button primary">Salvar</button>
    </div>
</form>
</dialog>