package databasewriter.core.tables.unloadingprocesstable;

import databasewriter.core.tables.AbstractTable;
import databasewriter.logging.DataWriterLoggerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A table that hold an info about ship unloading
 */
public class UnloadingProcessTable extends AbstractTable {
  static protected Logger LOGGER = DataWriterLoggerManager.getInstance().getLoggerDb();

  private final String ID = "id";
  private final String CRANE_NUMBER = "crane_num";
  private final String SHIP = "ship_name";
  private final String INITIAL_MASS = "init_mass";
  private final String CURRENT_MASS = "curr_mass";

  public UnloadingProcessTable(String aTableName) {
    super(aTableName);
  }

  @Override
  protected String makeCreateTableStatement(String tName) {
    return "CREATE TABLE " + getTableName() + " (" +
            ID + " int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            CRANE_NUMBER + " int," +
            SHIP + " varchar(30)," +
            INITIAL_MASS + " int," +
            CURRENT_MASS + " int)";
  }

  /**
   * Gets a list of table rows by result set
   *
   * @param resultSet the result set
   * @return the list of table rows
   * @throws SQLException
   */
  private List<TableRow> getRowsByResultSet(ResultSet resultSet) throws SQLException {
    List<TableRow> rows = new ArrayList<>();
    while (resultSet.next()) {
      TableRow row = new TableRow(
              resultSet.getInt(ID),
              resultSet.getInt(CRANE_NUMBER),
              resultSet.getString(SHIP),
              resultSet.getInt(INITIAL_MASS),
              resultSet.getInt(CURRENT_MASS));
      rows.add(row);
    }
    return rows;
  }

  /**
   * Insert a row
   *
   * @param aCraneNum the crane number
   * @param aShipName the ship name
   * @param anInitMass the initial cargo mass
   * @param aCurrMass the current cargo mass
   * @return count oа inserted rows
   * @throws SQLException if an error occurs dur inserting
   */
  public int insert(int aCraneNum, String aShipName, int anInitMass, int aCurrMass) throws SQLException {
    PreparedStatement statement = getDataBase().getConnection().prepareStatement(
            "INSERT INTO " + getTableName() + " (crane_num, ship_name, init_mass, curr_mass) VALUES(?, ?, ?, ?)");
    statement.setInt(1, aCraneNum);
    statement.setString(2, aShipName);
    statement.setInt(3, anInitMass);
    statement.setInt(4, aCurrMass);
    LOGGER.log(Level.INFO, statement.toString());
    return statement.executeUpdate();
  }

  /**
   * Insert a row
   *
   * @param row the table row
   * @return count oа inserted rows
   * @throws SQLException if an error occurs dur inserting
   */
  public int insert(TableRow row) throws SQLException {
    return insert(row.getCraneNumber(), row.getShipName(), row.getInitialMass(), row.getCurrentMass());
  }

  /**
   * Select the row by id
   *
   * @param anId the row id
   * @return the table row
   * @throws SQLException if an error occurs dur selecting
   */
  public TableRow selectById(int anId) throws SQLException {
    PreparedStatement statement = getDataBase().getConnection()
            .prepareStatement("SELECT * FROM " + getTableName() +" WHERE " + ID + " = ?");
    statement.setInt(1, anId);
    ResultSet resultSet = statement.executeQuery();
    List<TableRow> rows = getRowsByResultSet(resultSet);
    return rows.size() == 1 ? rows.get(0) : null;
  }

  /**
   * Selects a rows from the table by ship name
   *
   * @param aShipName the ship name
   * @return the list of table rows
   * @throws SQLException if an error occurs dur selecting
   */
  public List<TableRow> selectByShipName(String aShipName) throws SQLException {
    PreparedStatement statement = getDataBase().getConnection()
            .prepareStatement("SELECT * FROM " + getTableName() +" WHERE " + SHIP + " = ?");
    statement.setString(1, aShipName);
    ResultSet resultSet = statement.executeQuery();
    return getRowsByResultSet(resultSet);
  }
}
