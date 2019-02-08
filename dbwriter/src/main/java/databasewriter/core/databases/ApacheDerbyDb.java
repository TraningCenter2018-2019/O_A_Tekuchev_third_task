package databasewriter.core.databases;

import java.sql.SQLException;

/**
 * The Apache Derby database
 */
public class ApacheDerbyDb extends AbstractDb {

  /**
   * A constructor
   *
   * @param dbName the db name
   * @throws SQLException if connection was not established
   */
  public ApacheDerbyDb(String dbName) throws SQLException {
    super(dbName);
  }

  @Override
  protected String createConnectionString(String dbName) {
    return "jdbc:derby:memory:" + dbName + ";create=true";
  }
}
