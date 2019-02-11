import databasewriter.core.databases.AbstractDb;
import databasewriter.core.databases.ApacheDerbyDb;
import org.junit.*;
import databasewriter.core.tables.unloadingprocesstable.TableRow;
import databasewriter.core.tables.unloadingprocesstable.UnloadingProcessTable;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDbWork {
  static private final String TABLE_NAME = "unload_process";

  static private AbstractDb dataBase;
  static private UnloadingProcessTable table;

  @BeforeClass
  static public void init() throws SQLException {
    table = new UnloadingProcessTable(TABLE_NAME);
    dataBase = new ApacheDerbyDb("testdb");
    dataBase.addTable(table);
  }

  @AfterClass
  static public void close() throws SQLException {
    dataBase.closeConnection();
  }

  @Test
  public void testTableSelection() {
    assertEquals(table, dataBase.getTable(TABLE_NAME));
  }

  @Test
  public void testInsertSelect() throws SQLException {
    TableRow row = new TableRow(1, "1", 1, 1);
    int insertedCount = table.insert(row);
    assertEquals(insertedCount, 1);
    List<TableRow> a = table.selectByShipName("1");
    assertEquals(a.size(), 1);
    assertTrue(a.get(0).equalRowsWithoutId(row));
  }
}
