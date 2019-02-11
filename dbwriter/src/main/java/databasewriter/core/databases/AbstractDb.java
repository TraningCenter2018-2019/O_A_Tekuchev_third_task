package databasewriter.core.databases;

import databasewriter.core.tables.AbstractTable;
import databasewriter.logging.DataWriterLoggerManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a connection to a database
 */
public abstract class AbstractDb {
  static protected Logger LOGGER = DataWriterLoggerManager.getInstance().getLoggerDb();

  private String databaseName;
  private Connection connection;
  private String connectionString;
  private List<AbstractTable> tables;

  /**
   * A constructor
   *
   * @param dbName the db name
   * @throws SQLException if connection was not established
   */
  public AbstractDb(String dbName) throws SQLException {
    databaseName = dbName;
    connectionString = createConnectionString(databaseName);
    LOGGER.log(Level.INFO, "Connect to: " + connectionString);
    connection = createConnection(connectionString);
    tables = new ArrayList<>();
  }

  /**
   * Creates a connection string for db
   *
   * @param dbName the db name
   * @return the connection string
   */
  protected abstract String createConnectionString(String dbName);

  /**
   * Creates a connection with db
   *
   * @param connStr the connection string
   * @return a db connection
   * @throws SQLException if connection was not established
   */
  protected Connection createConnection(String connStr) throws SQLException {
    return DriverManager.getConnection(connStr);
  }

  /**
   * Gets the connection string
   * @return the connection string
   */
  protected final String getConnectionString() {
    return connectionString;
  }

  /**
   * Gets the connection
   * @return the connection
   */
  public final Connection getConnection() {
    return connection;
  }

  /**
   * Gest the db name
   * @return the db name
   */
  public final String getDatabaseName() {
    return databaseName;
  }

  /**
   * Adds a table to the db
   *
   * @param aTable the new table
   * @throws SQLException if an error occures due to adding table
   */
  public void addTable(AbstractTable aTable) throws SQLException {
    Statement statement = connection.createStatement();
    String createStatement = aTable.getCreateTableStatement();
    LOGGER.log(Level.INFO, "Add table: " + createStatement);
    statement.executeUpdate(createStatement);
    aTable.setDataBase(this);
    tables.add(aTable);
  }

  /**
   * Gets a table in the db by its name
   *
   * @param tableName the name of the table
   * @return the table or null if db doesn't have table with this name
   */
  public AbstractTable getTable(String tableName) {
    for (AbstractTable table : tables) {
      if (table.getTableName().equals(tableName)) {
        return table;
      }
    }
    return null;
  }

  /**
   * Closes connection with the db
   *
   * @throws SQLException  an error occures due to closing table
   */
  public void closeConnection() throws SQLException {
    connection.close();
  }

  /**
   * Whether the db is connected
   * @return true if it is
   * @throws SQLException
   */
  public boolean isConnectionClosed() throws SQLException {
    return connection.isClosed();
  }
}
