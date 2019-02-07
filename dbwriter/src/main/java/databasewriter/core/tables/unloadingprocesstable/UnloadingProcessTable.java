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

public class UnloadingProcessTable extends AbstractTable {
  static protected Logger LOGGER = DataWriterLoggerManager.getInstance().getLoggerDb();


  private final String ID_COL_NAME = "id";
  private final String craneNumber = "crane_num";
  private final String shipName = "ship_name";
  private final String initialMass = "init_mass";
  private final String currentMass = "curr_mass";

  public UnloadingProcessTable(String aTableName) {
    super(aTableName);
  }

  @Override
  protected String makeCreateTableStatement(String tName) {
    return "CREATE TABLE " + getTableName() + " (" +
            ID_COL_NAME + " int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            craneNumber + " int," +
            shipName    + " varchar(30)," +
            initialMass + " int," +
            currentMass + " int)";
  }

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

  public int insert(TableRow row) throws SQLException {
    return insert(row.getCraneNumber(), row.getShipName(), row.getInitialMass(), row.getCurrentMass());
  }

  private List<TableRow> getRowsByResultSet(ResultSet resultSet) throws SQLException {
    List<TableRow> rows = new ArrayList<>();
    while (resultSet.next()) {
      TableRow row = new TableRow(
              resultSet.getInt(ID_COL_NAME),
              resultSet.getInt(craneNumber),
              resultSet.getString(shipName),
              resultSet.getInt(initialMass),
              resultSet.getInt(currentMass));
      rows.add(row);
    }
    return rows;
  }

  public TableRow selectById(int anId) throws SQLException {
    PreparedStatement statement = getDataBase().getConnection()
            .prepareStatement("SELECT * FROM " + getTableName() +" WHERE " + ID_COL_NAME + " = ?");
    statement.setInt(1, anId);
    ResultSet resultSet = statement.executeQuery();
    List<TableRow> rows = getRowsByResultSet(resultSet);
    return rows.size() == 1 ? rows.get(0) : null;
  }

  public List<TableRow> selectByShipName(String aShipName) throws SQLException {
    PreparedStatement statement = getDataBase().getConnection()
            .prepareStatement("SELECT * FROM " + getTableName() +" WHERE " + shipName + " = ?");
    statement.setString(1, aShipName);
    ResultSet resultSet = statement.executeQuery();
    return getRowsByResultSet(resultSet);
  }
}
