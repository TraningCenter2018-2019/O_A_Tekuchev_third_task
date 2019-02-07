package databasewriter.core.tables;

import databasewriter.core.databases.AbstractDb;
import databasewriter.logging.DataWriterLoggerManager;

import java.util.logging.Logger;

public abstract class AbstractTable {
  static protected Logger LOGGER = DataWriterLoggerManager.getInstance().getLoggerDb();

  private String tableName;
  private AbstractDb dataBase;
  private String createTableStatement;

  public AbstractTable(String aTableName) {
    tableName = aTableName;
    createTableStatement = makeCreateTableStatement(tableName);
  }

  protected abstract String makeCreateTableStatement(String tName);

  public final String getTableName() {
    return tableName;
  }

  public final AbstractDb getDataBase() {
    return dataBase;
  }

  public final void setDataBase(AbstractDb dataBase) {
    this.dataBase = dataBase;
  }

  public final String getCreateTableStatement() {
    return createTableStatement;
  }
}
