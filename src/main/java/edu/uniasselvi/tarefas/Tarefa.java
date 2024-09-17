package edu.uniasselvi.tarefas;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public record Tarefa(Integer id, String titulo, LocalDate dataExecucao, StatusTarefa status) {
  public static Tarefa newTarefa(
      String idValue, String tituloValue, String dataExecucaoValue, String statusValue) throws IllegalArgumentException {
    if (Objects.isNull(tituloValue)
        || tituloValue.isBlank()
        || Objects.isNull(dataExecucaoValue)
        || dataExecucaoValue.isBlank()
        || Objects.isNull(statusValue)
        || statusValue.isBlank()) {
      throw new IllegalArgumentException("Values cannot be null or empty");
    }

    LocalDate dataExecutacao;
    try {
      dataExecutacao = LocalDate.parse(dataExecucaoValue);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid data execucao");
    }

    var id = Integer.MIN_VALUE;

    if (Objects.nonNull(idValue)) {
      try {
        id = Integer.parseInt(idValue);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid id value");
      }
    }

    if (tituloValue.length() < 5) {
      throw new IllegalArgumentException("Titulo deve conter no minimo 5 digitos");
    }

    StatusTarefa status;
    try {
      status = StatusTarefa.valueOf(statusValue);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid status value");
    }

    return new Tarefa(id, tituloValue, dataExecutacao, status);
  }
}
