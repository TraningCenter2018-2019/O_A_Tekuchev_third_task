package databasewriter.core.databases;

import java.sql.SQLException;

public class ApacheDerbyDb extends AbstractDb {

  public ApacheDerbyDb(String dbName) throws SQLException {
    super(dbName);
  }

  @Override
  protected String createConnectionString(String dbName) {
    return "jdbc:derby:memory:" + dbName + ";create=true";
  }

}
