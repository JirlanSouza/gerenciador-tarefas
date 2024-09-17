package edu.uniasselvi.tarefas;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class TarefaDao {
  private DatabaseConnection dataSource;

  public TarefaDao() {}

  @Inject
  public TarefaDao(DatabaseConnection dataSource) {
    this.dataSource = dataSource;
  }

  public List<Tarefa> findAll() {
    try (var connection = dataSource.getConnection()) {
      var statement = connection.createStatement();
      var result = statement.executeQuery("SELECT * FROM tarefas ORDER BY id");
      List<Tarefa> tarefas = new ArrayList<>();

      while (result.next()) {
        tarefas.add(
            new Tarefa(
                result.getInt("id"),
                result.getString("titulo"),
                result.getDate("data_execucao").toLocalDate(),
                StatusTarefa.valueOf(result.getString("status"))));
      }

      return tarefas;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return List.of();
    }
  }

  public Optional<Tarefa> findById(int id) {
    try (var connection = dataSource.getConnection()) {
      var statement = connection.prepareStatement("SELECT * FROM tarefas WHERE id = ?");
      statement.setInt(1, id);
      var result = statement.executeQuery();

      if (result.next()) {
        var tarefa =
            new Tarefa(
                result.getInt("id"),
                result.getString("titulo"),
                result.getDate("data_execucao").toLocalDate(),
                StatusTarefa.valueOf(result.getString("status")));

        return Optional.ofNullable(tarefa);
      }

      return Optional.empty();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return Optional.empty();
    }
  }

  public Optional<Tarefa> findByTitulo(String titulo) {
    try (var connection = dataSource.getConnection()) {
      var statement = connection.prepareStatement("SELECT * FROM tarefas WHERE titulo = ?");
      statement.setString(1, titulo);
      var result = statement.executeQuery();

      if (result.next()) {
        var tarefa =
            new Tarefa(
                result.getInt("id"),
                result.getString("titulo"),
                result.getDate("data_execucao").toLocalDate(),
                StatusTarefa.valueOf(result.getString("status")));

        return Optional.ofNullable(tarefa);
      }

      return Optional.empty();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return Optional.empty();
    }
  }

  public void save(Tarefa tarefa) {
    try (var connection = dataSource.getConnection()) {
      var preparedStatement =
          connection.prepareStatement(
              "INSERT INTO tarefas (titulo, data_execucao, status) VALUES (?, ?, ?)");
      preparedStatement.setString(1, tarefa.titulo());
      preparedStatement.setDate(2, Date.valueOf(tarefa.dataExecucao()));
      preparedStatement.setString(3, tarefa.status().toString());
      System.out.println(preparedStatement);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void update(Tarefa tarefa) {
    try (var connection = dataSource.getConnection()) {
      var preparedStatement =
          connection.prepareStatement(
              "UPDATE tarefas SET titulo = ?, data_execucao = ?, status = ? WHERE id = ?");
      preparedStatement.setString(1, tarefa.titulo());
      preparedStatement.setDate(2, Date.valueOf(tarefa.dataExecucao()));
      preparedStatement.setString(3, tarefa.status().toString());
      preparedStatement.setInt(4, tarefa.id());
      System.out.println(preparedStatement);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void delete(int id) {
    try (var connection = dataSource.getConnection()) {
      var preparedStatement = connection.prepareStatement("DELETE FROM tarefas WHERE id = ?");
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
