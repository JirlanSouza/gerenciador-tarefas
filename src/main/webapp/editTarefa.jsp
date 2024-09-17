<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<jsp:useBean id="tarefa" scope="request" type="edu.uniasselvi.tarefas.Tarefa"/>
<jsp:useBean id="statusList" scope="request" type="java.util.List<java.lang.String>"/>

<h2>Editar tarefa</h2>
<form id="edit-tarefa-form">
    <label for="titulo">Título</label>
    <input id="titulo" name="titulo" type="text" value="${tarefa.titulo()}" placeholder="Titulo">
    <label for="dataExecucao">Data execução</label>
    <input id="dataExecucao" name="dataExecucao" type="date" value="${tarefa.dataExecucao()}" placeholder="Data execução">
    <label for="status">Status</label>
    <select id="status" name="status">
        <c:forEach var="status" items="${statusList}">
            <option value="${status}"
                    <c:if test="${status == tarefa.status().name()}">selected</c:if>>${status}</option>
        </c:forEach>
    </select>

    <div id="modal-actions-buttons-box">
        <button onclick="editTarefaModal.close(); return false;" class="medium-button primary">Cancelar</button>
        <button type="submit" class="medium-button secondary">Salvar</button>
    </div>
</form>
