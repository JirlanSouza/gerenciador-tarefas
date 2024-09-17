package edu.uniasselvi.tarefas;

import jakarta.enterprise.context.RequestScoped;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@RequestScoped
public class DatabaseConnection {
  private Properties properties;

  public DatabaseConnection() {
    try {
      properties = getDatabaseProperties();
      Class.forName(properties.getProperty("driver-class"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Connection getConnection() {
    try {
      return DriverManager.getConnection(properties.get("url").toString(), properties);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Properties getDatabaseProperties() throws IOException {
    InputStream propertiesStream =
        getClass().getClassLoader().getResourceAsStream("database.properties");
    var properties = new Properties();
    properties.load(propertiesStream);
    return properties;
  }
}
