package edu.uniasselvi.tarefas;

import jakarta.inject.Inject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("/tarefas/*")
public class Tarefas extends HttpServlet {
  private final TarefaDao tarefaDao;

  @Inject
  public Tarefas(TarefaDao tarefaDao) {
    this.tarefaDao = tarefaDao;
  }

  public void init() {}

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("text/html");
    String requestURI = request.getRequestURI();
    String[] paths = requestURI.split("/");
    List<String> statusTarefaList =
        Arrays.stream(StatusTarefa.values()).map(StatusTarefa::name).toList();
    boolean isListTarefasPath = paths.length == 0 || paths[paths.length - 1].equals("tarefas");

    if (isListTarefasPath) {
      RequestDispatcher rd = request.getRequestDispatcher("/tarefas.jsp");
      List<Tarefa> tarefas = tarefaDao.findAll();
      request.setAttribute("tarefas", tarefas);
      request.setAttribute("statusList", statusTarefaList);
      rd.forward(request, response);
      return;
    }

    try {
      int tarefaId = Integer.parseInt(paths[paths.length - 1]);
      Optional<Tarefa> tarefa = tarefaDao.findById(tarefaId);

      if (tarefa.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
      }

      RequestDispatcher rd = request.getRequestDispatcher("/editTarefa.jsp");
      request.setAttribute("tarefa", tarefa.get());
      request.setAttribute("statusList", statusTarefaList);
      rd.forward(request, response);

    } catch (NumberFormatException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      var titulo = request.getParameter("titulo");
      var dataExecucao = request.getParameter("dataExecucao");
      var statusTarefa = request.getParameter("status");
      Tarefa tarefa = Tarefa.newTarefa(null, titulo, dataExecucao, statusTarefa);
      Optional<Tarefa> existentTarefa = tarefaDao.findByTitulo(titulo);

      if (existentTarefa.isPresent()) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        return;
      }

      tarefaDao.save(tarefa);
      response.sendRedirect(request.getContextPath());

    } catch (IllegalArgumentException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      String id = request.getParameter("id");
      var titulo = request.getParameter("titulo");
      var dataExecucao = request.getParameter("dataExecucao");
      var statusTarefa = request.getParameter("status");
      Tarefa tarefa = Tarefa.newTarefa(id, titulo, dataExecucao, statusTarefa);
      tarefaDao.update(tarefa);
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);

    } catch (IllegalArgumentException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  public void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      String id = request.getParameter("id");
      tarefaDao.delete(Integer.parseInt(id));
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    } catch (NumberFormatException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  public void destroy() {}
}
