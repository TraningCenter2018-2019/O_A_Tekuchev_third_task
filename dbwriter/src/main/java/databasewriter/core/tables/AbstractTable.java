package databasewriter.core.tables;

import databasewriter.core.databases.AbstractDb;
import databasewriter.logging.DataWriterLoggerManager;

import java.util.logging.Logger;

/**
 * The class that represents a table in a db and operations above it
 */
public abstract class AbstractTable {
  static protected Logger LOGGER = DataWriterLoggerManager.getInstance().getLoggerDb();

  private String tableName;
  private AbstractDb dataBase;
  private String createTableStatement;

  /**
   * A constructor
   *
   * @param aTableName the table name
   */
  public AbstractTable(String aTableName) {
    tableName = aTableName;
    createTableStatement = makeCreateTableStatement(tableName);
  }

  /**
   * Create an sql command to create this table
   *
   * @param tName the table name
   * @return the sql command to create table
   */
  protected abstract String makeCreateTableStatement(String tName);

  /**
   * Gets the table name
   *
   * @return the table name
   */
  public final String getTableName() {
    return tableName;
  }

  /**
   * Gets a database this table is belong
   *
   * @return the database or null if the table is not belong to any db
   */
  public final AbstractDb getDataBase() {
    return dataBase;
  }

  /**
   * Sets a database the table is belong
   *
   * @param dataBase the database
   */
  public final void setDataBase(AbstractDb dataBase) {
    this.dataBase = dataBase;
  }

  /**
   * Gets an sql command to create this table
   *
   * @return the sql command to create this table
   */
  public final String getCreateTableStatement() {
    return createTableStatement;
  }
}
