<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<jsp:useBean id="tarefas" scope="request" type="java.util.List<edu.uniasselvi.tarefas.Tarefa>"/>
<!DOCTYPE html>
<html>
<head>
    <title>Tarefas</title>
    <link type="text/css" rel="stylesheet" href="css/styles.css">
</head>
<header>
    <div class="container">
        <h1>Tarefas</h1>
    </div>
</header>
<body>

<main>
    <div class="container">
        <div class="actions-container">
            <h3>Lista de tarefas</h3>
            <button class="medium-button primary" onclick="openCreateTarefaDialog()">Adicionar tarefa</button>
        </div>
        <table>
            <thead>
            <tr>
                <th>Nome</th>
                <th>Data execução</th>
                <th>Status</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="tarefa" items="${tarefas}">
                <tr>
                    <td>${tarefa.titulo()}</td>
                    <td>${DateTimeFormatter.ofPattern("dd/MM/YYYY").format(tarefa.dataExecucao())} </td>
                    <td class="status-${tarefa.status().name()}">
                            ${tarefa.status().name()}
                    </td>
                    <td>
                        <button class="small-button primary"
                                onclick="editTarefaDialog(${tarefa.id()})">
                            Editar
                        </button>
                        <button class="small-button secondary" onclick="deleteTarefa(${tarefa.id()})">Excluir</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>
<div class="backdrop"/>
<dialog id="editModal"></dialog>
<jsp:include page="createTarefa.jsp"/>
<script>
    const baseUrl = "<%= request.getContextPath() %>";
    const backdrop = document.querySelector(".backdrop");
    const createTarefaModal = document.getElementById("create-tarefa-modal");
    const editTarefaModal = document.getElementById("editModal");
    let editTarefaForm = undefined;

    const displayBackdrop = () => backdrop.style.display = "block";
    const hiddenBackdrop = () => backdrop.style.display = "none";
    backdrop.addEventListener("click", () => {
        createTarefaModal.close();
        editTarefaModal.close();
    });
    createTarefaModal.addEventListener("click", ev => ev.stopPropagation());
    createTarefaModal.addEventListener("load", displayBackdrop)
    createTarefaModal.addEventListener("close", hiddenBackdrop);
    editTarefaModal.addEventListener("click", ev => ev.stopPropagation());
    editTarefaModal.addEventListener("close", hiddenBackdrop);

    const openCreateTarefaDialog = () => {
        createTarefaModal.show();
        displayBackdrop();
    }

    const editTarefaDialog = (id) => {
        fetch(window.location.origin + baseUrl + "/tarefas/" + id).then(response => {
            if (!response.ok) {
                throw new Error("Error ao buscar tarefa");
            }

            response.text().then(text => editTarefaModal.innerHTML = text);
        });
        editTarefaModal.show();
        displayBackdrop();

        setTimeout(() => {
            editTarefaForm = document.getElementById("edit-tarefa-form");
            editTarefaForm.addEventListener("submit", ev => editTarefaHandler(ev, id));
        }, 500);
    }

    function deleteTarefa(id) {
        fetch(window.location.origin + baseUrl + "/tarefas?id=" + id, {method: "delete"}).then(response => {
            if (!response.ok) {
                throw new Error("Error ao buscar tarefa");
            }

            window.location.reload();
        });
    }


    function editTarefaHandler(ev, id) {
        ev.preventDefault();
        const formData = new FormData(ev.target);

        fetch(window.location.origin + baseUrl + "/tarefas?id=" + id, {
            method: "put",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: new URLSearchParams(formData).toString()
        }).then(response => {
            if (!response.ok) {
                throw new Error("Error ao buscar tarefa");
            }

            editTarefaModal.close();
            window.location.reload();
        });
    }


</script>
</body>
</html>