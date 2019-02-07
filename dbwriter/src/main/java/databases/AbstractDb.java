package databases;

import tables.AbstractTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDb {
  private String databaseName;
  private Connection connection;
  private String connectionString;
  private List<AbstractTable> tables;

  public AbstractDb(String dbName) throws SQLException {
    databaseName = dbName;
    connectionString = createConnectionString(databaseName);
    connection = createConnection(connectionString);
    tables = new ArrayList<>();
  }

  protected abstract String createConnectionString(String dbName);

  protected Connection createConnection(String connStr) throws SQLException {
    return DriverManager.getConnection(connStr);
  }

  protected final String getConnectionString() {
    return connectionString;
  }

  public final Connection getConnection() {
    return connection;
  }

  public final String getDatabaseName() {
    return databaseName;
  }

  public void addTable(AbstractTable aTable) throws SQLException {
    Statement statement = connection.createStatement();
    String createStatement = aTable.getCreateTableStatement();
    statement.executeUpdate(createStatement);
    aTable.setDataBase(this);
    tables.add(aTable);
  }

  public AbstractTable getTable(String tableName) {
    for (AbstractTable table : tables) {
      if (table.getTableName().equals(tableName)) {
        return table;
      }
    }
    return null;
  }

  public void closeConnection() throws SQLException {
    connection.close();
  }

  public boolean isConnectionClosed() throws SQLException {
    return connection.isClosed();
  }
}
